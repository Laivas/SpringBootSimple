package com.spring.app.stomp;
//package com.webcrawler.app.stomp;
//
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.json.JSONObject;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.AbstractWebSocketHandler;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Component
//public class WebSocketHandlerImpl extends TextWebSocketHandler {
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) {
//       
//
//    	System.out.println(message.toString());
//    	
//    	System.out.println("\n");
//    	
//    	
//    	System.out.println(new String(message.asBytes()));
//    	
//		JSONObject object = new JSONObject(message.toString());
//		
//		System.out.println(object.get("sendMessage").toString());
//    	
//    	System.out.println(message.toString());
//    	
//    }
//    
//    @MessageMapping("/chat")
//	public String chatWs(@DestinationVariable TextMessage message) {
//		String time = new SimpleDateFormat("HH:mm").format(new Date());
//		System.out.println("here");
//		JSONObject object = new JSONObject(message.getPayload());
//		
//		System.out.println(object.toString());
//		
//		return "return";
//	}
//	
//	
//}
