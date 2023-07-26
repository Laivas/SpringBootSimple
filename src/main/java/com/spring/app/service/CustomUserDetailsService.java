package com.spring.app.service;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.model.User;
import com.spring.app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
//	@Override
//	@Transactional
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		
//		User user = userRepository.findUserByName(username);
//		
//		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
//				getAuthorities(user));
//		
//	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userRepository.findByEmail(email);

	    if (user == null) {
	        throw new UsernameNotFoundException("Not found!");
	    }

	    return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
				getAuthorities(user));
	}
	
	
    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
    	
    	Set<GrantedAuthority> grantedAuthoritySet = new HashSet<GrantedAuthority>();
    	
    	user.getRoles().forEach(role -> grantedAuthoritySet.add(new SimpleGrantedAuthority(role.getRole())));
    	
    	return grantedAuthoritySet;
    	
    }

}
