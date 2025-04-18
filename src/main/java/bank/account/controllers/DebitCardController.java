package bank.account.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.account.dto.DebitCardDTO;
import bank.account.entity.DebitCard;
import bank.account.service.DebitCardService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debit-cards")
@RequiredArgsConstructor
public class DebitCardController {
	private final DebitCardService debitCardService;

    @PostMapping
    public Mono<DebitCard> create(@RequestBody DebitCardDTO dto) {
        return debitCardService.createDebitCard(dto);
    }

    @GetMapping("/by-document/{doc}")
    public Flux<DebitCard> getByCustomerDocument(@PathVariable String doc) {
        return debitCardService.getCardsByCustomerDocument(doc);
    }

    @GetMapping("/{cardNumber}")
    public Mono<DebitCard> getByCardNumber(@PathVariable String cardNumber) {
        return debitCardService.getByCardNumber(cardNumber);
    }
}
