package com.spring.app.util;


import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class CustomPage {
	
	
	 public <T> Page<T> paginated(Pageable pageable , List<T> paginate) {
	    	
			
		  int pageSize = pageable.getPageSize();

		  int currentPage = pageable.getPageNumber();
		  
		  int startItem = currentPage * pageSize;
		  
//		  List<T> rooms = new ArrayList<T>();
//		 
//		  rooms = webSecurityConfig.getCurrentlyLoggedUser().getChatRooms();
		 
		  List<T> list;
		  	
			if (paginate.size() < startItem) {
				list = Collections.emptyList();
			} else {
				int toIndex = Math.min(startItem + pageSize, paginate.size());
				list = paginate.subList(startItem, toIndex);
			}
			 
			  
		Page<T> chatRoomPage = new PageImpl<T>(list, PageRequest.of(currentPage, pageSize) , paginate.size());

       return chatRoomPage;
       
   }
	

}
