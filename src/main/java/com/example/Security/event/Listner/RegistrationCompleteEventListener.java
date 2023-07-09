package com.example.Security.event.Listner;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.Security.User.User;
import com.example.Security.User.UserService;
import com.example.Security.event.RegistrationCompleteEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	private final UserService userservice;
	private final JavaMailSender mailSender;
	private User theUser;
	
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		
		
		/*
		 * 
		 * 
		 * 
		 *  5.send an email
		 */
		//1 . get the newly generated user 
		theUser = event.getUser();
		
		//2. create a verification token for the user
		String token = UUID.randomUUID().toString();
		
		//3.save the verification token 
		userservice.saveUserVerficationToken(theUser, token);
		
		//4. build the verification url to send to the userEmail
		
		String url = event.getApplicationUrl()+"/register/verifyEmail?token="+token;
		
		try {
			sendVerificationEmail(url);
		}
		catch(MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		log.info("click here to verify your registration : {} ",url);
	}
	
	public void sendVerificationEmail(String url) throws MessagingException , UnsupportedEncodingException {
		String subject = "Email verification";
		String senderName = "User registraction Portal service";
		String mailContent = "<p> Hi, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("crazyjoe1432@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
	}



}
