package com.example.demo.exceptions;

public class BillingAlreadyExistsException extends RuntimeException{

	public BillingAlreadyExistsException(String message) {
		super(message);
	}
}
