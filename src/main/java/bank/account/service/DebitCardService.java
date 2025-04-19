package bank.account.service;

import bank.account.dto.DebitCardDTO;
import bank.account.dto.MainAccountBalanceDTO;
import bank.account.entity.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {
	Mono<DebitCard> createDebitCard(DebitCardDTO dto);
    Flux<DebitCard> getCardsByCustomerDocument(String documentNumber);
    Mono<DebitCard> getByCardNumber(String cardNumber);
    Mono<DebitCard> associateDebitCardToAllAccounts(String cardNumber);
    Mono<DebitCard> assignMainAccount(String cardNumber, String mainAccountNumber);
    Mono<MainAccountBalanceDTO> getMainAccountBalance(String cardNumber);
}
