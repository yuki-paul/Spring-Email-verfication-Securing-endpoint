package com.example.Security.Registeration;

import java.util.Calendar;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Security.Registeration.token.VerificationToken;
import com.example.Security.Registeration.token.VerificationTokenRepository;
import com.example.Security.User.User;
import com.example.Security.User.UserRepository;
import com.example.Security.User.UserService;
import com.example.Security.event.RegistrationCompleteEvent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
	private final UserRepository userRepo;
	private final VerificationTokenRepository tokenRepo;
	private final UserService userservice;
	private final ApplicationEventPublisher publisher;
	
	@PostMapping
	public String registerUser(@RequestBody RegistrationRequest Registerrequest, final HttpServletRequest request) {
		//registering the user into the db
		User user  = userservice.registerUser(Registerrequest);
		
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		
		return "Success please check your mail to complete registeration";
	}
	
	@GetMapping("/verifyEmail")
	public String verifyEmail(@RequestParam("token") String token) {
		VerificationToken theToken = tokenRepo.findByToken(token);
		if(theToken.getUser().isEnabled()==true) {
			return "this account is already been verified please Login";
		}
		String verificationResult = userservice.verifyToken(token);
		if(verificationResult.equalsIgnoreCase("valid")) {
			return "Email verification is done now Please login";
		}
		return "Invalid verification ";
	}
	
	
	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}

}
