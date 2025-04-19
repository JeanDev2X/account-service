package bank.account.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.account.dto.DebitCardDTO;
import bank.account.dto.MainAccountBalanceDTO;
import bank.account.entity.Account;
import bank.account.entity.DebitCard;
import bank.account.repository.AccountRepository;
import bank.account.repository.DebitCardRepository;
import bank.account.service.DebitCardService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService{
	@Autowired
	private final DebitCardRepository debitCardRepository;
	@Autowired
    private final AccountRepository accountRepository;
	
	@Override
	public Mono<DebitCard> createDebitCard(DebitCardDTO dto) {
		DebitCard card = DebitCard.builder()
	            .cardNumber(dto.getCardNumber())
	            .accountNumbers(dto.getAccountNumbers()) // aquí usamos la lista
	            .customerDocumentNumber(dto.getCustomerDocumentNumber())
	            .createdAt(LocalDate.now())
	            .build();
	    return debitCardRepository.save(card);
	}

	@Override
	public Flux<DebitCard> getCardsByCustomerDocument(String documentNumber) {
		return debitCardRepository.findByCustomerDocumentNumber(documentNumber);
	}

	@Override
	public Mono<DebitCard> getByCardNumber(String cardNumber) {
		return debitCardRepository.findByCardNumber(cardNumber);
	}

	@Override
	public Mono<DebitCard> associateDebitCardToAllAccounts(String cardNumber) {
		return debitCardRepository.findByCardNumber(cardNumber)
		        .flatMap(card ->
		            accountRepository.findByDocumentNumber(card.getCustomerDocumentNumber())
		                .map(Account::getAccountNumber)
		                .collectList()
		                .flatMap(accountNumbers -> {
		                    card.setAccountNumbers(accountNumbers);
		                    return debitCardRepository.save(card);
		                })
		        );
	}

	@Override
	public Mono<DebitCard> assignMainAccount(String cardNumber, String mainAccountNumber) {
		return debitCardRepository.findByCardNumber(cardNumber)
	            .flatMap(card -> {
	                if (!card.getAccountNumbers().contains(mainAccountNumber)) {
	                    return Mono.error(new IllegalArgumentException("La cuenta principal debe estar entre las cuentas asociadas"));
	                }
	                card.setMainAccountNumber(mainAccountNumber);
	                return debitCardRepository.save(card);
	            });
	}

	@Override
	public Mono<MainAccountBalanceDTO> getMainAccountBalance(String cardNumber) {
		return debitCardRepository.findByCardNumber(cardNumber)
	            .flatMap(card -> {
	                String mainAccount = card.getMainAccountNumber();
	                if (mainAccount == null || mainAccount.isEmpty()) {
	                    return Mono.error(new IllegalStateException("No hay cuenta principal asociada a esta tarjeta"));
	                }
	                return accountRepository.findByAccountNumber(mainAccount)
	                        .map(account -> new MainAccountBalanceDTO(mainAccount, account.getBalance()));
	            });
	}
	
}
