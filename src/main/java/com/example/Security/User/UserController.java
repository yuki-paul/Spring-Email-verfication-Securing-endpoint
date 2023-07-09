package com.example.Security.User;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
 private final UserService userservice;
 
 @GetMapping("/all")
 public List<User> getUsers(){
	 return userservice.getUsers();
 }
 
 
}
