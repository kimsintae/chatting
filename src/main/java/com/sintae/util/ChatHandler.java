package com.sintae.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sintae.chatting.ChatController;
import com.sintae.vo.Message;

public class ChatHandler extends TextWebSocketHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);
	
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("afterConnectionEstablished (접속자수 ) "+ sessionList.size());
		sessionList.add(session);
		
//		for(WebSocketSession user : sessionList) {
//			user.sendMessage(new TextMessage(sessionList.toString()
//					.replace("WebSocketServerSockJsSession", "")
//					.replace("[", "")
//					.replace("]", "")
//					.replace("id=", "")
//					)
//					);
//			//getId 의 세션값은 브라우저의 고유 값이기 때문에 다른 사용자가 입장해도 고유 세션값은  브라우저마다 동일하다.
//		}
		
		
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info("handleTextMessage called : "+message.getPayload());
		
		/*
		 *  user : 내 자신
		 *  session : 상대방
		 */
		
		
		ObjectMapper om = new ObjectMapper();
		Message msg = om.readValue(message.getPayload(), Message.class);
		
			for(WebSocketSession user: sessionList) {
				
				switch (msg.getType()) {
				case "enter"://입장시
					String enter_text = user.getId().equals(session.getId()) ? " 님 환영합니다. 즐거운 채팅 되세요 ^^" : " 님이 입장하셨습니다. ";
					user.sendMessage(new TextMessage(msg.getName()+enter_text));
					break;
				case "msg"://채팅시
					String msg_style = user.getId().equals(session.getId()) ? "'font-weight:bold;color:red;'" : "color:black;'";
					user.sendMessage(new TextMessage(msg.getName()+" : <span style="+msg_style+">"+msg.getMessage()+"</span>"));
					
					break;	
				case "exit"://퇴장시
					user.sendMessage(new TextMessage(msg.getName()+" 님이 퇴장하셨습니다. "));
					break;
				}
				
			}
		}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info("afterConnectionClosed called ");
		
		sessionList.remove(session);
	}
}
