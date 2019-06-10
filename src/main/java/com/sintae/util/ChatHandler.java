package com.sintae.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sintae.vo.Message;

public class ChatHandler extends TextWebSocketHandler{
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("afterConnectionEstablished (접속자수 ) "+ sessionList.size());
		sessionList.add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("handleTextMessage called : "+message.getPayload());
		
		
		ObjectMapper om = new ObjectMapper();
		Message msg = om.readValue(message.getPayload(), Message.class);
		
		
			for(WebSocketSession sess: sessionList) {
				
				switch (msg.getType()) {
				case "enter"://입장시
					sess.sendMessage(new TextMessage(msg.getName()+" 님이 입장하셨습니다. "));
					break;
				case "msg"://채팅시
					sess.sendMessage(new TextMessage(msg.getName()+" : "+msg.getMessage()));
					break;	
				case "exit"://퇴장시
					sess.sendMessage(new TextMessage(msg.getName()+" 님이 퇴장하셨습니다. "));
					break;
				}
				
			}
		}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("afterConnectionClosed called ");
		sessionList.remove(session);
	}
}
