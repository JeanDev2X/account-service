package com.bank.account.service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

import com.bank.account.service.entity.AccountType;

@Data
@Builder
public class AccountResponse {
	private String id;
	private String accountNumber;
    private String documentNumber;
    private BigDecimal balance;
    private AccountType type;
}
