package com.example.demo.exceptions;

public class AccountAlreadyExistsException extends RuntimeException{

	public AccountAlreadyExistsException(String message) {
		super(message);
	}
}
