package bank.account.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import bank.account.dto.DebitCardDTO;
import bank.account.entity.DebitCard;
import bank.account.repository.DebitCardRepository;
import bank.account.service.DebitCardService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService{
	
	private final DebitCardRepository debitCardRepository;
	
	@Override
	public Mono<DebitCard> createDebitCard(DebitCardDTO dto) {
		DebitCard card = DebitCard.builder()
                .cardNumber(dto.getCardNumber())
                .accountNumber(dto.getAccountNumber())
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
	
}
