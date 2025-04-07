package com.bank.account.service.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accounts")
public class Account {
	@Id
    private String id;
	@NotBlank
    private String accountNumber; //numero de cuenta
	private BigDecimal balance;//saldo
    private String documentNumber;
    private AccountType type;
    private List<String> holders;//titulares
    private List<String> signers;//firmantes
}
