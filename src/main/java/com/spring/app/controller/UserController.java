package com.spring.app.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.model.User;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.WebSecurityConfig;
import com.spring.app.service.RoleService;
import com.spring.app.service.UserService;

import jakarta.validation.Valid;



@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
//	@Autowired
//	private RegistrationEmailManager registrationEmailManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WebSecurityConfig websecurityConfig;
	
	
    @GetMapping("/signup")
    public String showSignUpForm(User user, Model model) {
    	
        return "signup";
        
    }
    
    @PostMapping("/signup")
    public String addUser(@Valid User user, BindingResult result, Model model) {
    	
        if (result.hasErrors()) {
        	
        	System.out.println(result.hasErrors());
        	
            return "signup";
           
        }
        
        if(userService.checkIfUserByNameExists(user.getName())) {
        	
        	result.addError(new FieldError("user", "name", "Name already exists"));
        	
        	 return "signup";
        	
        }
        
        if(userService.checkIfUserAlreadyExists(user.getEmail())) {
        	
        	result.addError(new FieldError("user", "email", "Email already exists"));
        	
        	 return "signup";
        	
        }

    	   
        user.setPassword(websecurityConfig.encodePassword(user.getPassword()));
        
        user.setRoles(roleService.createDefaultRoleUser());
        
        userRepository.save(user);
        
//        registrationEmailManager.sendEmailToUser(user);
        
        System.out.println("user saved");
        
        return "user-added";
        
    }
    
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String userSettings(Model model) {
    	
    	model.addAttribute("user" , websecurityConfig.getCurrentlyLoggedUser());
    	
    	return "settings";
    	
    }
    
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String changeUserPassword(Model model, User user, @RequestParam String newPassword , @RequestParam String confirmNewPassword) {
    	
    	if(websecurityConfig.passwordEncoder().matches(user.getPassword(), websecurityConfig.getCurrentlyLoggedUser().getPassword())) {
    		
    		if(newPassword.matches(confirmNewPassword)) {
    		
    		userRepository.findUserById(websecurityConfig.getCurrentlyLoggedUser().getId()).setPassword(websecurityConfig.encodePassword(newPassword));
    		
    		userRepository.save(user);
    		
    		return "settings";
    		
    		}
    		
    	}
    	
    	return "settings";
    }
    
    
    @GetMapping("/admin/user-list")
    public String showUserList(Model model) {
    	
        model.addAttribute("users", userRepository.findAll());
        
        return "user-list";
    }
    
    @GetMapping("/user-list")
    public String showUsersList(Model model) {
    	
        model.addAttribute("users", userRepository.findAll());
        
        return "user-list-users";
    }
    
    @GetMapping("/admin/update-user/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    	
        User user = userRepository.findUserById((long) id);
        
        model.addAttribute("user", user);
        
        return "update-user";
    }
    
    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable("id") Long id, User user, BindingResult result, Model model) {
    	
        if (result.hasErrors()) {
//            user.setId(id);
            
            return "settings";
        }
        
        if(user.getEmail().isEmpty() || user.getName().isEmpty()) {
        	
        	result.addError(new FieldError("user", "email", "Email must be not empty!"));
        	
        	
        	return "redirect:/settings";
        	
        }
        
        if(websecurityConfig.passwordEncoder().matches(user.getPassword(), websecurityConfig.getCurrentlyLoggedUser().getPassword())) {
        	
        User userToUpdate = userRepository.findById(id);
        
        userToUpdate.setEmail(user.getEmail());
        
        userToUpdate.setName(user.getName());
        
        userRepository.save(userToUpdate);
        
        return "redirect:/settings";
        
        }
        
        return "redirect:/settings";
    }
    
   
    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
    	
         User user = userRepository.findById(id)
        		
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        userService.removeUserFromFriendsList(user); 
         
        userService.deleteUser(user);
        
        return "redirect:/admin/user-list";
    }
	

}
