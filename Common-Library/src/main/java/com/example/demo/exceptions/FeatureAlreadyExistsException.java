package com.example.demo.exceptions;

public class FeatureAlreadyExistsException extends RuntimeException{

	public FeatureAlreadyExistsException(String message) {
		super(message);
	}
}
