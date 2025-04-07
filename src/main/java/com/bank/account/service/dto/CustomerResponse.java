package com.bank.account.service.dto;
import lombok.Data;

@Data
public class CustomerResponse {
	private String id;
    private String documentNumber;
    private String type; // "PERSONAL" o "BUSINESS"
}
