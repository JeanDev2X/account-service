package bank.account.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import bank.account.entity.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String>{
	Flux<DebitCard> findByCustomerDocumentNumber(String documentNumber);
    Mono<DebitCard> findByCardNumber(String cardNumber);
}	
