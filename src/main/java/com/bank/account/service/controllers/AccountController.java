package com.bank.account.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bank.account.service.dto.AccountResponse;
import com.bank.account.service.entity.Account;
import com.bank.account.service.service.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	@Autowired
    private AccountService service;

    @PostMapping
    public Mono<Account> create(@RequestBody Account account) {
        return service.createAccount(account);
    }

    @GetMapping
    public Flux<Account> getAll() {
        return service.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Mono<AccountResponse> getAccountById(@PathVariable String id) {
    	
        return service.getById(id)
                .map(account -> AccountResponse.builder()
                        .id(account.getId())
                        .documentNumber(account.getDocumentNumber())
                        .balance(account.getBalance())
                        .build());
    }

    @PutMapping("/{id}")
    public Mono<Account> update(@PathVariable String id, @RequestBody Account account) {
    	account.setId(id);
        return service.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteAccount(id);
    }
    
    @GetMapping("/by-account-number/{accountNumber}")
    public Mono<Account> getByAccountNumber(@PathVariable String accountNumber) {
        return service.getByAccountNumber(accountNumber);
    }
    
}
