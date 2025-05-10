package com.example.demo.exceptions;

public class FeatureNotFoundException extends RuntimeException {

	public FeatureNotFoundException(String message) {
		super(message);
	}
}
