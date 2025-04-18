package bank.account.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "debit_cards")
public class DebitCard {
	@Id
    private String id;
    private String cardNumber;
    private String accountNumber; // Cuenta asociada
    private String customerDocumentNumber; // Dueño de la tarjeta
    private LocalDate createdAt;
}
