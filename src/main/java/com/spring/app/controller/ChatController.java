package com.spring.app.controller;


import java.security.Principal;
import java.sql.Date;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.app.model.ChatRoom;
import com.spring.app.model.MessageStomp;
import com.spring.app.model.User;
import com.spring.app.repository.ChatRoomRepository;
import com.spring.app.security.WebSecurityConfig;
import com.spring.app.service.ChatRoomService;
import com.spring.app.service.MessageService;

@Controller
public class ChatController {

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

//	@Autowired
//	private UserRepository userRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private WebSecurityConfig webSecurityConfig;
	
	

	@RequestMapping(value="/chat/{id}")
	public String openOldChat(@PathVariable("id") Long id, Model model) {
		
		ChatRoom chatRoom = null;
		
		chatRoom = chatRoomService.openOldChat(id);
		
		model.addAttribute("messages", chatRoomService.loadMessasge(chatRoom));
		
		model.addAttribute("ChatRoom", chatRoom);
						

		return "chat";

	}

//	@PostMapping("/chat/{id}")
//	public String sendMessage(@PathVariable("id") Long id, Model model, String sendMessage) {
//
////		 User user = userRepository.findUserByName(webSecurityConfig.getAuthentication().getName());
//		
//		 User user = webSecurityConfig.getCurrentlyLoggedUser(); 
//		
//		 ChatRoom chatRoom = chatRoomRepository.findChatRoomById(id);
//		 
//		 messageService.addMessageToChatRoom(chatRoom, sendMessage, user);
//		 
//		 model.addAttribute("ChatRoom", chatRoom);	
//		 
//		 model.addAttribute("messages", chatRoomService.loadMessasge(chatRoom));
//		 
//
//		return "chat";
//
//	}

	@GetMapping("/deleteChat/{id}")
	public String deleteChatRoom(@PathVariable("id") Long id, Model model) {
		
		User user = webSecurityConfig.getCurrentlyLoggedUser();	

		ChatRoom chatRoom = chatRoomRepository.findChatRoomById(id);
		
		chatRoomService.deleteChatRoomWithMessages(chatRoom, user);
		
		model.addAttribute("chatroom", user.getChatRooms());

		return "redirect:/chatRooms";
		
	}
	

	@RequestMapping(value="/chat/{id}" , method = RequestMethod.POST)
	public String openFriendChat(@PathVariable("id") Long id, @RequestParam String chatRoomName , Model model) {
		
			System.out.println(chatRoomName);
		
			if(chatRoomName == null) {
				
				System.out.println("chatRoomName is null");
				return "redirect:/friendList";
			}
			
			if(chatRoomName.isEmpty()) {
				
				chatRoomName = "Default Chat";
			}
			
		ChatRoom chatRoom = chatRoomService.openFriendChat(id, chatRoomName);
		
		model.addAttribute("messages", chatRoomService.loadMessasge(chatRoom));
		
		model.addAttribute("ChatRoom", chatRoom);
		
		return "redirect:/chat" + "/" + chatRoom.getId();
	}
	
	
	@MessageMapping("/chat/{chatId}")
	@SendTo("/queue/chat/{chatId}")
	public MessageStomp chatMessage(@Payload String message , Principal user) {
		
		JSONObject object = new JSONObject(new JSONTokener(message));
		
		System.out.println(message);
		
		object.toMap().entrySet().stream().forEach(e -> System.out.println(e.getKey()));		

		messageService.addMessage(object.getString("chatId"),
				object.getString("sendMessage"),
				object.getString("messageSenderName"),
				String.valueOf(object.getLong("date")));
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		MessageStomp messageStomp = null;

		try {
			messageStomp = mapper.readValue(message, MessageStomp.class);
			
			messageStomp.setDate(new Date(object.getLong("date")).toString());
			
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return messageStomp;
	}
	
	
	
	

}
