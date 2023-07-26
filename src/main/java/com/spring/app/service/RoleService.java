package com.spring.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.spring.app.model.Role;


@Service
public class RoleService {
	
	
	public Set<Role> createDefaultRoleUser() {
		
		Role role = new Role();
		
        role.setRole("ROLE_USER");
		
		Set<Role> roles = new HashSet<Role>();
		
        roles.add(role);
        
        return roles;
		
	}

}
