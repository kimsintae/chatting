package com.sintae.util;

import java.util.ArrayList;
import java.util.HashMap;
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

import javafx.scene.text.Text;

public class ChatHandler extends TextWebSocketHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);
	
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	private HashMap<String, String> userList = new HashMap<String, String>();
	private List<String> users = null; // 채팅 접속자 리스트
	private Message msg = null;
	private ObjectMapper om = new ObjectMapper();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("1. afterConnectionEstablished");
		// 접속했을 떄
		sessionList.add(session);
		//getId 의 세션값은 브라우저의 고유 값이기 때문에 다른 사용자가 입장해도 고유 세션값은  브라우저마다 동일하다.
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("2. handleTextMessage");
		
		msg = om.readValue(message.getPayload(), Message.class);
		
		
		
		userList.put(session.getId(), msg.getName());
		users =  new ArrayList<String>(userList.values());
		/*
		 *  user : 내 자신
		 *  session : 상대방
		 */
		
			for(WebSocketSession user: sessionList) {
				
				switch (msg.getType()) {
				case "enter"://입장시
					String enter_text = user.getId().equals(session.getId()) ? " 님 환영합니다. 즐거운 채팅 되세요 ^^" : " 님이 입장하셨습니다. ";
					String target = user.getId().equals(session.getId()) ? "me" : "other";
					user.sendMessage(new TextMessage("{\"type\":\"enter\",\"name\":\""+msg.getName()+"\",\"others\":"+om.writeValueAsString(userList)+",\"text\":\""+msg.getName()+enter_text+"\",\"target\":\""+target+"\"}"));
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
		System.out.println("3. afterConnectionClosed");

		// 퇴장한 유저이름
		String outUser = userList.get(session.getId());
		
		userList.remove(session.getId());
		sessionList.remove(session);
		for(WebSocketSession user : sessionList) {
			user.sendMessage(new TextMessage("{\"type\":\"exit\",\"outUser\":\""+outUser+"\",\"others\":"+om.writeValueAsString(userList)+",\"text\":\""+outUser+"님이 퇴장하셨습니다.\"}"));
		}
		
	}
	
//	@Override
//	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//		System.out.println("handleMessage");
//		
//		message.getPayload();
//	}
	
	
}
