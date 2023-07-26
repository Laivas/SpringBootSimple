package com.spring.app.controller;

import java.io.IOException;
import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.model.Photo;
import com.spring.app.security.WebSecurityConfig;
import com.spring.app.service.PhotoService;

@Controller
public class UploadController {
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private WebSecurityConfig webSecurityConfig;
	
//	@Autowired
//	private PhotoRepository photoRepository;
	
	
	@GetMapping("/uploadPhoto")
	public String uploadPhotoPage() {
		
		return "redirect:/friends-list";
		
	}
	
	@PostMapping("/uploadPhoto")
	public String uploadPhoto(Model model, @RequestParam("image") MultipartFile file) {
		
		if(file.isEmpty()) {
			
			return "redirect:/photos";
			
		}
		
		for(Photo photo : webSecurityConfig.getCurrentlyLoggedUser().getPhotos()) {
			
			if(photo.getName().equals(file.getOriginalFilename())) {
			
			return "redirect:/photos";
			
			}
			
			try {
				if(Arrays.equals(photo.getImageBytes(), file.getBytes())) {
					
					return "redirect:/photos";
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		
		try {
			photoService.savePhoto(file.getBytes(), file.getOriginalFilename(), webSecurityConfig.getCurrentlyLoggedUser());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/photos";
		
	}
	

}
