package com.bank.account.service.exception;


public class RequestException extends RuntimeException {

	public RequestException(String message)
	{
	  super(message);
	}
	
	public RequestException(String message, Throwable cause)
	{
	  super(message, cause);
	}
	
	
}
