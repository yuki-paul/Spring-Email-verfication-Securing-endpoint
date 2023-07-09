package com.example.Security.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Security.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class userDetailsService implements UserDetailsService {

	private final UserRepository userrepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		return userrepo.findByEmail(email).map(UserRegistrationDetails :: new)
				.orElseThrow(() ->  new UsernameNotFoundException("user not found"));
	}

}
