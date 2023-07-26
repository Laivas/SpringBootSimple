package com.spring.app.service;

import java.io.InputStream;
import java.sql.Blob;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LobService {

	@Autowired
	private SessionFactory sessionFactory;

	
    public Blob createBlob(InputStream content, long size) {
        return sessionFactory.getCurrentSession().getLobHelper().createBlob(content, size);
        
    }
   
    public Blob createBlobFromByteArray(byte[] input) {
    	 
    	 return getCurrentSession().getLobHelper().createBlob(input);
    	
    }
    
    public Session getCurrentSession() {
    	
    	Session session;

    	try {
    	    session = sessionFactory.getCurrentSession();
    	} catch (HibernateException e) {
    	    session = sessionFactory.openSession();
    	}
    	
    	 return session;
    	
    }
	
}
