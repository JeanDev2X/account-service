package com.bank.account.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import com.bank.account.service.dto.CustomerResponse;
import com.bank.account.service.entity.Account;
import com.bank.account.service.entity.AccountType;
import com.bank.account.service.repository.AccountRepository;
import com.bank.account.service.service.AccountService;

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
                            } else if ("BUSINESS".equalsIgnoreCase(customer.getType())) {
                                if (account.getType() == AccountType.SAVINGS || account.getType() == AccountType.FIXED_TERM) {
                                    return Mono.error(new IllegalStateException("A business customer cannot have savings or fixed-term accounts"));
                                }
                            }
                            return repository.save(account);
                        }));
    }

    private Mono<CustomerResponse> validateCustomerByDocumentNumber(String documentNumber) {
    	return customerWebClient.get()
                .uri("/customers/document/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .flatMap(customer -> {
                    if (customer.getId() == null || customer.getId().isBlank()
                     || customer.getDocumentNumber() == null || customer.getDocumentNumber().isBlank()
                     || customer.getType() == null || customer.getType().isBlank()) {
                        return Mono.error(new RuntimeException("Customer not found with document number: " + documentNumber));
                    }
                    return Mono.just(customer);
                });
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

}
