package bank.account.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebitCardDTO {
	private String cardNumber;
	private List<String> accountNumbers;
    private String customerDocumentNumber;

    @JsonCreator
    public DebitCardDTO(
            @JsonProperty("cardNumber") String cardNumber,
            @JsonProperty("accountNumber") List<String> accountNumber,
            @JsonProperty("customerDocumentNumber") String customerDocumentNumber) {
        this.cardNumber = cardNumber;
        this.accountNumbers = accountNumbers;
        this.customerDocumentNumber = customerDocumentNumber;
    }
}
