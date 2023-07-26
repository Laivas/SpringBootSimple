package com.spring.app.stomp;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
/* @EnableWebSocket */
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer   {

//	  @Autowired
//	  private WebSocketHandlerImpl webSocketHandlerImpl;
		
	
//	  @Override
//	  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(new WebSocketHandlerImpl(), "/chat").setAllowedOrigins("*").withSockJS();
//	}
	  
	    @Override
	    public void configureMessageBroker(MessageBrokerRegistry config) {
	        config.enableSimpleBroker("/topic", "/queue");
	        config.setApplicationDestinationPrefixes("/app");

	    }

	    @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	        registry.addEndpoint("/chatroom").withSockJS();
	    }
	
	
//	@Bean
//	public ServletServerContainerFactoryBean createWebSocketContainer() {
//		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//		container.setMaxTextMessageBufferSize(8192);
//		container.setMaxBinaryMessageBufferSize(8192);
//		return container;
//	}
//
	  
	  
	  
	  
	  
	  
	  
	  
//	@Override
//	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//
//		registry.addHandler(WebSocketHandlerImpl(), "/")
//				.addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
//
//	}
//
//	private WebSocketHandler WebSocketHandlerImpl() {
//		// TODO Auto-generated method stub
//		return new WebSocketHandlerImpl();
//	}

}
