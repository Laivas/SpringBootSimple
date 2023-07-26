package com.spring.app.model;


import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class Message {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private ChatRoom chatRoom;
	
	private String messageBody;
	
	@ManyToOne
	private User messageSender;
	
	private Date date;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public User getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(User messageSender) {
		this.messageSender = messageSender;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
