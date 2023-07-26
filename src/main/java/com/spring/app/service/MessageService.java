package com.spring.app.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.app.model.ChatRoom;
import com.spring.app.model.Message;
import com.spring.app.model.User;
import com.spring.app.repository.ChatRoomRepository;
import com.spring.app.repository.MessageRepository;
import com.spring.app.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public Message addMessageToChatRoom(ChatRoom chatRoom, String msg, User user) {
		
		Message message = new Message();
		
		message.setChatRoom(chatRoom);
		
		message.setMessageBody(msg);
		
		message.setMessageSender(user);
		
		message.setDate(new Date(System.currentTimeMillis()));
		
		messageRepository.save(message);
		
		return message;
	}
	
public Message addMessage(String id, String msg, String userName, String date) {
		
		Message message = new Message();
		
		message.setChatRoom(chatRoomRepository.findChatRoomById(Long.valueOf(id)));
		
		message.setMessageBody(msg);
		
		message.setMessageSender(userRepository.findUserByName(userName));
		
		message.setDate(new Date(Long.valueOf(date)));
		
		messageRepository.save(message);
		
		return message;
	}
	
	
	
	public void removeMessagesFromChatRoom(ChatRoom chatRoom) {
		
		chatRoomRepository.findChatRoomById(chatRoom.getId()).getMessages().clear();
		
		messageRepository.findMessagesByChatRoomId(chatRoom.getId());

	}
	
	

}
