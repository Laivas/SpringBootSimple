package com.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.model.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	public User findByEmail(String email);
	
	public User findUserById(Long id);
	
	public User findUserByName(String userName);
	
	public User findById(Long id);
	
	public void deleteById(Long id);
	
}