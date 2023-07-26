package com.spring.app.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.app.model.User;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.WebSecurityConfig;

import jakarta.transaction.Transactional;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WebSecurityConfig webSecurityConfig;
	
	
	public boolean checkIfUserAlreadyExists(String email) {
				
		if(userRepository.findByEmail(email) != null) {
			
			return true;
			
		}else
		
			return false;
		
	}
	
	public boolean checkIfUserByNameExists(String name) {
		
		if(userRepository.findUserByName(name) != null) {
			
			return true;
			
		}else
		
			return false;
		
	}
	
	public String getUserEmail() {
		
		User user = userRepository.findUserByName(webSecurityConfig.getAuthentication().getName());
		
		return user.getEmail();
	};
	
	
	public List<User> addUserToFriendsList(User user) {
		
		List<User> users = null;
		
		if(checkIfUserByNameExists(user.getName())) {
		
		userRepository.findUserByName(webSecurityConfig.getAuthentication().getName()).getFriends().add(user);		
		
		users = userRepository.findUserByName(webSecurityConfig.getAuthentication().getName()).getFriends();
		
		}
		
		return users;
		
	}
	@Transactional
	public void removeUserFromFriendsList(User user) {
		
		userRepository.findUserByName(webSecurityConfig.getAuthentication().getName()).getFriends().remove(user);
		
	}
	@Transactional
	public void deleteUser(User user) {
		
		
		userRepository.deleteById(user.getId());
		
	}

}
