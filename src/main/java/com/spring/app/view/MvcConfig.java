package com.spring.app.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/signup").setViewName("signup");
		registry.addViewController("/user-added").setViewName("user-added");
		registry.addViewController("/update-user").setViewName("update-user");
		registry.addViewController("/user-list").setViewName("user-list");
		registry.addViewController("/chat").setViewName("chat");
		registry.addViewController("/friends-list").setViewName("friendList");
		registry.addViewController("/photos").setViewName("photos");
		registry.addViewController("/photo-gallery").setViewName("photo-gallery");
		registry.addViewController("/settings").setViewName("settings");


	}

}