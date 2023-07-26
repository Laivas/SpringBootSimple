package com.spring.app.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.spring.app.model.User;

@Service
public class RegistrationEmailManager implements EmailManager {

	@Autowired
	private JavaMailSender mailSender;

	
//	private SimpleMailMessage templateMailMessage;
	 

//	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
//		this.templateMailMessage = simpleMailMessage;
//	}

	@Override
	public void sendEmailToUser(User user) {
		// TODO Auto-generated method stub

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(user.getEmail());

		message.setText("Hello " + user.getName() + " your registration sucessfull.");

		this.mailSender.send(message);

		System.out.println("Email sent to : " + user.getEmail());

	}
	
	

}
