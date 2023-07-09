package com.example.Security.Registeration.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

	VerificationToken findByToken(String token);
}
