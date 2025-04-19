package bank.account.entity;

import java.time.LocalDate;
import java.util.List;
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
    private List<String> accountNumbers; // <- ahora es una lista de cuentas asociadas
    private String customerDocumentNumber; // Dueño de la tarjeta
    private String mainAccountNumber; //cuenta principal
    private LocalDate createdAt;
}
