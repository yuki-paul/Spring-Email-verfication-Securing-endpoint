package com.example.Security.Registeration;

public record RegistrationRequest(
		String firstName,
		String lastName, 
		String email, 
		String role, 
		String password) {

}
