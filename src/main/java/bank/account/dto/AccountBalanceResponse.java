package bank.account.dto;

import java.math.BigDecimal;
import java.util.List;

import bank.account.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBalanceResponse {
	private String accountNumber;
    private String documentNumber;
    private BigDecimal balance;
    private AccountType type;
    private List<String> holders;
    private List<String> signers;
}
