package com.bank.account.service.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bank.account.service.dto.AccountBalanceResponse;
import com.bank.account.service.dto.AccountRequest;
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
	public Mono<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
	    Account account = new Account();
	    account.setAccountNumber(request.getAccountNumber());
	    account.setDocumentNumber(request.getDocumentNumber());
	    account.setType(request.getType());
	    account.setBalance(request.getBalance());
	    account.setInitialAmount(request.getInitialAmount());
	    account.setHolders(request.getHolders());
	    account.setSigners(request.getSigners());

	    return service.createAccount(account)
	            .map(this::toResponse);
	}
	
	private AccountResponse toResponse(Account account) {
	    return AccountResponse.builder()
	            .id(account.getId())
	            .accountNumber(account.getAccountNumber())
	            .documentNumber(account.getDocumentNumber())
	            .type(account.getType())
	            .balance(account.getBalance())
	            .initialAmount(account.getInitialAmount())
	            .build();
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
    
    @GetMapping("/document/{documentNumber}")
    public Flux<AccountBalanceResponse> getAccountsByDocument(@PathVariable String documentNumber) {
        return service.getAccountsByDocumentNumber(documentNumber);
    }
    
}
