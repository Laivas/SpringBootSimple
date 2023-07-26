package com.spring.app.mail;

import com.spring.app.model.User;

public interface EmailManager {
	
	void sendEmailToUser(User user);

}
