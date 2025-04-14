package bank.account.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import bank.account.entity.AccountType;
import lombok.Data;

@Data
public class AccountRequest {
	@NotBlank
    private String accountNumber;

    @NotBlank
    private String documentNumber;

    @NotNull
    private AccountType type;
    
    private BigDecimal balance;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal initialAmount;

    private List<String> holders;

    private List<String> signers;
}
