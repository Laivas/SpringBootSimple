package com.spring.app.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.app.model.ChatRoom;
import com.spring.app.model.Message;
import com.spring.app.model.User;
import com.spring.app.repository.ChatRoomRepository;
import com.spring.app.repository.MessageRepository;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.WebSecurityConfig;



@Service
public class ChatRoomService {
	
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private WebSecurityConfig webSecurityConfig;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public ChatRoom openOldChat(Long id) {
		
		return chatRoomRepository.findChatRoomById(id);
		
	}
	
	public ChatRoom addMessageToChatRoom(String message, ChatRoom chatRoom) {
		
		Message msg = new Message();
		
		msg.setChatRoom(chatRoom);
		
		msg.setMessageBody(message);
		
		List<Message> messages = chatRoom.getMessages();
		
		messages.add(msg);		
		
		messageRepository.save(msg);
		
		return chatRoom;
	}
	
    public Page<ChatRoom> paginated(Pageable pageable) {
    	
		
		  int pageSize = pageable.getPageSize();

		  int currentPage = pageable.getPageNumber();
		  
		  int startItem = currentPage * pageSize;
		  
		  List<ChatRoom> rooms = new ArrayList<ChatRoom>();
		 
		  rooms = webSecurityConfig.getCurrentlyLoggedUser().getChatRooms();
		 
		  List<ChatRoom> list;
		  	
			if (rooms.size() < startItem) {
				list = Collections.emptyList();
			} else {
				int toIndex = Math.min(startItem + pageSize, rooms.size());
				list = rooms.subList(startItem, toIndex);
			}
			 
			  
		Page<ChatRoom> chatRoomPage = new PageImpl<ChatRoom>(list, PageRequest.of(currentPage, pageSize) , rooms.size());

        return chatRoomPage;
        
    }
	
	public ArrayList<Message> loadMessasge(ChatRoom chatRoom) {
		
		return messageRepository.findMessagesByChatRoomId(chatRoom.getId());
		
	}
	
	public ArrayList<String> loadMessagesUserNames(ChatRoom chatRoom) {
		
		ArrayList<String> users = new ArrayList<String>();
		
		chatRoomRepository.findChatRoomById(chatRoom.getId())
		.getMessages()
		.stream()
		.forEach(message -> users.add(message.getMessageSender().getName()));	
		
		return users;
	}
	
	
	public ArrayList<ChatRoom> loadChatRoomsByUserId(Long userId) {
		
		
		return chatRoomRepository.findChatRoomByUsersId(userId);
		
	}
	
	public void addUserToChatRoom(ChatRoom chatRoom, User user) {
		
		chatRoomRepository.findById(chatRoom.getId()).get().getUsers().add(user);
		
	}
	
	public void removeUserFromChatRoom(ChatRoom chatRoom, User user) {
		
		chatRoomRepository.findById(chatRoom.getId()).get().getUsers().remove(user);
	}
	
	public List<User> loadFriendsList() {
		
		List<User> users = webSecurityConfig.getCurrentlyLoggedUser().getFriends();
	    	
//		Collections.sort(users, 
//                (u1, u2) -> u1.getName().compareTo(u2.getName()));
		
		return users;
	}
	
	public boolean chatRoomByNameExists(String chatRoomName) {
		
		if(chatRoomRepository.findChatRoomByTopic(chatRoomName) != null) {
			
			return true;
			
		}else
			
		return false;
		
	}
	
	public ChatRoom createChatRoom(User creator, String chatRoomName, List<User> users) {
		
		
		ChatRoom chatRoom = new ChatRoom();
		
		chatRoom.setTopic(chatRoomName);
		
		chatRoom.setUsers(users);
		
		userRepository.findUserByName(creator.getName()).getChatRooms().add(chatRoom);

		chatRoomRepository.save(chatRoom);
		
		userRepository.save(creator);
		
		return chatRoom;
		
	}
	
	public ChatRoom openFriendChat(Long friendId, String chatRoomName) {
		
		
		List<User> users = new ArrayList<>();
		
		users.add(userRepository.findUserById(friendId));
		
		users.add(webSecurityConfig.getCurrentlyLoggedUser());
		
			System.out.println(new Date(System.currentTimeMillis())+" Users list size : " + users.size());
		
		return createChatRoom(webSecurityConfig.getCurrentlyLoggedUser(), chatRoomName, users);
		
	}
	
	public List<String> usersInChatRoom(Long chatRoomId){
		
		List<String> users = new ArrayList<String>();
		
		chatRoomRepository.findChatRoomById(chatRoomId).getUsers().stream().forEach(user -> users.add(user.getName()));
		
		return users;
		
	}
	
	public String getUsersInChatRoom(Long chatRoomId) {
		
		List<String> users = new ArrayList<>();
		
		chatRoomRepository.findChatRoomById(chatRoomId).getUsers().stream().forEach(user -> users.add(user.getName()));
			
		String listAsString = "";

		for(String user : users) {
			
			listAsString += user + "\n";
			
		}
		
		return listAsString;
		
	}
	
	public List<ChatRoom> availableChats() {
		
		List<ChatRoom> chatRooms = new ArrayList<>();		

		webSecurityConfig.getCurrentlyLoggedUser().getChatRooms().stream().forEach(room -> chatRooms.add(room));
		
			System.out.println(new Date(System.currentTimeMillis()) +" chat Room size : " + chatRooms.size());
			
		return chatRooms;
		
	}
	
	public void deleteChatRoomWithMessages(ChatRoom chatRoom, User user) {
		
		messageRepository.findMessagesByChatRoomId(chatRoom.getId()).stream().forEach(message -> messageRepository.delete(message));
		
		
		chatRoomRepository.delete(chatRoom);
		
		
	}

	
	
}
