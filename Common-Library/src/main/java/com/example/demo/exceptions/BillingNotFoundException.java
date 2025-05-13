package com.example.demo.exceptions;

public class BillingNotFoundException extends RuntimeException {

	public BillingNotFoundException(String message) {
		super(message);
	}
}
