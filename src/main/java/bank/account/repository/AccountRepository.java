package bank.account.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import bank.account.entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
	Flux<Account> findByDocumentNumber(String documentNumber);
	Mono<Account> findByAccountNumber(String accountNumber);
}
