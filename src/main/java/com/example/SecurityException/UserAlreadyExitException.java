package com.example.SecurityException;

public class UserAlreadyExitException extends RuntimeException {
	
	public UserAlreadyExitException(String message) {
		super(message);
	}

}
