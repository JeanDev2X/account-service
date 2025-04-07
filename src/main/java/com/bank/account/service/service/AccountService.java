package com.bank.account.service.service;

import com.bank.account.service.entity.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
	Mono<Account> createAccount(Account account);
    Flux<Account> getAllAccounts();
    Mono<Account> getById(String id);
    Mono<Account> updateAccount(String id, Account account);
    Mono<Void> deleteAccount(String id);
    Mono<Account> getByAccountNumber(String accountNumber);
}
