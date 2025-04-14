package bank.account.dto;
import lombok.Data;

@Data
public class CustomerResponse {
	private String id;
    private String documentNumber;
    private String type; // "PERSONAL" o "BUSINESS"
    private String profile; // VIP, PYME, etc.
    private Boolean hasCreditCard;
}
