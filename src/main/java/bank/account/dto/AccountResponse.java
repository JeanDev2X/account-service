package bank.account.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

import bank.account.entity.AccountType;

@Data
@Builder
public class AccountResponse {
	private String id;
	private String accountNumber;
    private String documentNumber;
    private AccountType type;
    private BigDecimal balance;
    private BigDecimal initialAmount;
}
