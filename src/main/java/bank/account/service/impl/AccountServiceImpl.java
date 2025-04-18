package bank.account.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import bank.account.dto.AccountBalanceResponse;
import bank.account.dto.CustomerResponse;
import bank.account.entity.Account;
import bank.account.entity.AccountType;
import bank.account.repository.AccountRepository;
import bank.account.service.AccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
    private AccountRepository repository;
    
    private final WebClient customerWebClient;

    @Autowired
    public AccountServiceImpl(WebClient customerWebClient) {
        this.customerWebClient = customerWebClient;
    }

    @Override
    public Mono<Account> createAccount(Account account) {
    	return validateCustomerByDocumentNumber(account.getDocumentNumber())
                .flatMap(customer -> repository.findByDocumentNumber(account.getDocumentNumber())
                        .collectList()
                        .flatMap(existingAccounts -> {
                            if ("PERSONAL".equalsIgnoreCase(customer.getType())) {
                                if (account.getType() == AccountType.SAVINGS || account.getType() == AccountType.FIXED_TERM) {
                                    boolean alreadyHasAccount = existingAccounts.stream()
                                            .anyMatch(a -> a.getType() == account.getType());
                                    if (alreadyHasAccount) {
                                        return Mono.error(new IllegalStateException("A personal customer can only have one " + account.getType() + " account"));
                                    }
                                }

                                if ("VIP".equalsIgnoreCase(customer.getProfile())) {
                                    if (account.getType() != AccountType.SAVINGS) {
                                        return Mono.error(new IllegalArgumentException("VIP customers can only open savings accounts"));
                                    }
                                    if (account.getInitialAmount() == null || account.getInitialAmount().compareTo(new BigDecimal("500.00")) < 0) {
                                        return Mono.error(new IllegalArgumentException("VIP accounts require a minimum initial amount of 500.00"));
                                    }
                                    if (!Boolean.TRUE.equals(customer.getHasCreditCard())) {
                                        return Mono.error(new IllegalArgumentException("VIP customers must have a credit card to open a savings account"));
                                    }
                                }

                            } else if ("BUSINESS".equalsIgnoreCase(customer.getType())) {
                                if (account.getType() == AccountType.SAVINGS || account.getType() == AccountType.FIXED_TERM) {
                                    return Mono.error(new IllegalStateException("A business customer cannot have savings or fixed-term accounts"));
                                }

                                if ("PYME".equalsIgnoreCase(customer.getProfile())) {
                                    if (account.getType() != AccountType.CURRENT) {
                                        return Mono.error(new IllegalArgumentException("PYME customers can only open current accounts"));
                                    }
                                    if (!Boolean.TRUE.equals(customer.getHasCreditCard())) {
                                        return Mono.error(new IllegalArgumentException("PYME customers must have a credit card to open a current account"));
                                    }
                                }
                            }

                            if (account.getInitialAmount() == null || account.getInitialAmount().compareTo(BigDecimal.ZERO) < 0) {
                                return Mono.error(new IllegalArgumentException("The opening amount must be zero or greater"));
                            }

                            return hasOverdueCredits(account.getDocumentNumber())
                            	    .flatMap(hasOverdue -> {
                            	        if (hasOverdue) {
                            	            return Mono.error(new IllegalStateException("Customer has overdue credits and cannot open new products."));
                            	        }
                            	        return repository.save(account);
                            	    });
                        }));
    }

    private Mono<CustomerResponse> validateCustomerByDocumentNumber(String documentNumber) {
        return customerWebClient.get()
                .uri("/customers/document/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .flatMap(customer -> {
                    if (customer.getId() == null || customer.getDocumentNumber() == null || customer.getType() == null) {
                        return Mono.error(new RuntimeException("Customer not found with document number: " + documentNumber));
                    }
                    return Mono.just(customer);
                });
    }
    
    private Mono<Boolean> hasOverdueCredits(String documentNumber) {
        Mono<Boolean> overdueLoans = customerWebClient.get()
                .uri("/credits/has-overdue-loans/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false); // fallback en caso de error

        Mono<Boolean> overdueCreditCards = customerWebClient.get()
                .uri("/credits/has-overdue-credit-cards/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false); // fallback en caso de error

        return Mono.zip(overdueLoans, overdueCreditCards)
                .map(tuple -> tuple.getT1() || tuple.getT2()); // true si tiene deuda vencida en cualquiera
    }

    @Override
    public Flux<Account> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public Mono<Account> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Account> updateAccount(String id, Account account) {
        return repository.findById(id)
                .flatMap(existingAccount -> {
                    existingAccount.setBalance(account.getBalance());
                    existingAccount.setHolders(account.getHolders());
                    existingAccount.setSigners(account.getSigners());
                    return repository.save(existingAccount);
                });
    }

    @Override
    public Mono<Void> deleteAccount(String id) {
        return repository.deleteById(id);
    }
    
    @Override
    public Mono<Account> getByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")));
    }
    
    @Override
    public Flux<AccountBalanceResponse> getAccountsByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
                .map(account -> AccountBalanceResponse.builder()
                        .accountNumber(account.getAccountNumber())
                        .documentNumber(account.getDocumentNumber())
                        .balance(account.getBalance())
                        .type(account.getType())
                        .holders(account.getHolders())
                        .signers(account.getSigners())
                        .build());
    }

}
