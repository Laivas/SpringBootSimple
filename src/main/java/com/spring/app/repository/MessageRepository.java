package com.spring.app.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	public ArrayList<Message> findMessagesByChatRoomId(Long id);
	
}
