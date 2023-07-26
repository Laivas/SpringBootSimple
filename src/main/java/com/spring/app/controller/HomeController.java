package com.spring.app.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.model.ChatRoom;
import com.spring.app.model.User;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.WebSecurityConfig;
import com.spring.app.service.ChatRoomService;
import com.spring.app.service.PhotoService;
import com.spring.app.service.UserService;
import com.spring.app.util.PagePagination;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
	
	@Autowired
	private ChatRoomService chatRoomService;

//	@Autowired
//	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WebSecurityConfig webSecurityConfig;
	
	@Autowired
	private PhotoService photoService;
	

	
	@GetMapping("/chatRooms")
	public String showChatRoomList(Model model , @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
	
		
			int currentPage = page.orElse(0);
	        
	        int pageSize = size.orElse(10); 
					
	        Page<ChatRoom> chatRooms =  chatRoomService.paginated(PageRequest.of(currentPage, pageSize));

	        PagePagination<ChatRoom> pagePagination = new PagePagination<ChatRoom>(chatRooms, "/chatRooms");
	        
	        model.addAttribute("page", pagePagination);
		
			model.addAttribute("chatRoomService", chatRoomService);
			
//			model.addAttribute("chatrooms" , chatRoomService.availableChats());
			model.addAttribute("chatrooms" , chatRooms);
			
			return "chat-rooms";
		
	}
	
	@GetMapping("/friendList")
	public String showFriendsList(Model model) {		
				
		model.addAttribute("users" , photoService.loadFriendsListWithData());
		
		return "friends-list";
		
	}
	
	@PostMapping("/addFriend")
	public String addFriend(@RequestParam(name="friendEmail") String friendEmail, Model model) {        
        
		User user = webSecurityConfig.getCurrentlyLoggedUser();
		
		if(webSecurityConfig.getCurrentlyLoggedUser().getEmail().equals(friendEmail)) {
			
        	System.out.println("User email is same as currently logged user email.");

        	model.addAttribute("users" , user.getFriends());
        	
        	return "redirect:/friendList";
			
			
		}
		
        if(!userService.checkIfUserAlreadyExists(friendEmail)) {
        	
        	System.out.println("not exist");

        	model.addAttribute("users" , user.getFriends());
        	
        	return "redirect:/friendList";
        	
        }
        
        if(user.getFriends().contains(userRepository.findByEmail(friendEmail))) {
        	
        	model.addAttribute("users" , user.getFriends());
        	
        	return "redirect:/friendList";
        	
        }
		
		User friend = userRepository.findByEmail(friendEmail);
		
		user.getFriends().add(friend);
		
		userRepository.save(user);
		
		model.addAttribute("users" , user.getFriends());
		
		return "redirect:/friendList";
	}
	
	@GetMapping("/removeFriend/{id}")
	public String removeFriend(@PathVariable("id") Long id , Model model) {
		
		User user = webSecurityConfig.getCurrentlyLoggedUser();	
		
		user.getFriends().remove(userRepository.findById(id));
		
		userRepository.save(user);
		
		model.addAttribute("users" , user.getFriends());
		
		return "redirect:/friendList";
		
	}
	
	

}
