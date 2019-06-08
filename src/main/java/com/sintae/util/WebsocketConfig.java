package com.sintae.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketConfigurer{
	@Autowired
	private ChatHandler chatHandler;
	
	@Autowired
	private RoomHandler roomHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//웹소켓 주소 설정
		//핸들러와 매핑
		registry.addHandler(roomHandler, "/roomChat").addHandler(chatHandler, "/chat").setAllowedOrigins("*");

	}
}
