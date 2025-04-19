package bank.account.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainAccountBalanceDTO {
	private String mainAccountNumber;
    private BigDecimal balance;
}
