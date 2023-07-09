package com.example.Security.User;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Security.Registeration.RegistrationRequest;
import com.example.Security.Registeration.token.VerificationToken;
import com.example.Security.Registeration.token.VerificationTokenRepository;
import com.example.SecurityException.UserAlreadyExitException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepo;
	
	private final PasswordEncoder passwordEncoder;
	
	private final VerificationTokenRepository verficationTokenRepo;
	
	
	public List<User> getUsers(){
		return userRepo.findAll();
	}
	
	public Optional<User> findUserByEmail(String email){
		return userRepo.findByEmail(email);
	}
	
	public User registerUser(RegistrationRequest registerRequest) {
		Optional<User> user = userRepo.findByEmail(registerRequest.email());
		if(user.isPresent()) {
			throw new UserAlreadyExitException("user with email " + registerRequest.email() + "already exits ");
		}
		var newUser = new User();
		newUser.setFirstName(registerRequest.firstName());
		newUser.setLastName(registerRequest.lastName());
		newUser.setEmail(registerRequest.email());
		newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
		newUser.setRole(registerRequest.role());
		
		return userRepo.save(newUser);
		
	}
	
	public void saveUserVerficationToken(User theUser, String token) {
		var verificationToken = new VerificationToken(token , theUser);
		verficationTokenRepo.save(verificationToken);
	}
	
	
	public String verifyToken( String token){
		
		VerificationToken theToken = verficationTokenRepo.findByToken(token);
		
		if(token==null){
			return "in-valid";
		}
		User user = theToken.getUser();
		Calendar calendar = Calendar.getInstance();
		if(theToken.getExpireTime().getTime() - calendar.getTime().getTime()<=0) {
			verficationTokenRepo.delete(theToken);
			return "the token already Expired";
		}
		user.setEnabled(true);
		userRepo.save(user);
		return "valid";
		
	}
		

}
