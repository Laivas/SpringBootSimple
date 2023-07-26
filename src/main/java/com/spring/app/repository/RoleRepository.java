package com.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.spring.app.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	public Role findByRole(String roleName);


}
