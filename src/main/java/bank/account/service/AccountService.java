package bank.account.service;

import bank.account.dto.AccountBalanceResponse;
import bank.account.entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
	Mono<Account> createAccount(Account account);
    Flux<Account> getAllAccounts();
    Mono<Account> getById(String id);
    Mono<Account> updateAccount(String id, Account account);
    Mono<Void> deleteAccount(String id);
    Mono<Account> getByAccountNumber(String accountNumber);
    Flux<AccountBalanceResponse> getAccountsByDocumentNumber(String documentNumber);
}
