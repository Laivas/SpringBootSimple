package com.spring.app.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

	public ChatRoom findChatRoomById(Long id);
	
	public ArrayList<ChatRoom> findChatRoomByUsersId(Long userId);
	
	public ChatRoom findChatRoomByTopic(String topic);
	
}
