package bank.account.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DebitCardDTO {
	private String cardNumber;
    private String accountNumber;
    private String customerDocumentNumber;

    @JsonCreator
    public DebitCardDTO(
            @JsonProperty("cardNumber") String cardNumber,
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("customerDocumentNumber") String customerDocumentNumber) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.customerDocumentNumber = customerDocumentNumber;
    }
}
