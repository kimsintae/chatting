package com.sintae.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sintae.chatting.ChatController;
import com.sintae.vo.Message;

public class UserHandler extends TextWebSocketHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
	
	
	//������ ���
	private List<WebSocketSession> onUser = new ArrayList<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("afterConnectionEstablished called ");
		onUser.add(session);
		
		for(WebSocketSession user : onUser) {
			user.sendMessage(new TextMessage(onUser.toString()
					.replace("WebSocketServerSockJsSession", "")
					.replace("[", "")
					.replace("]", "")
					.replace("id=", "")
					)
					);
			//getId �� ���ǰ��� �������� ���� ���̱� ������ �ٸ� ����ڰ� �����ص� ���� ���ǰ���  ���������� �����ϴ�.
		}
		
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info("handleTextMessage called : "+message.getPayload());
		
		ObjectMapper om = new ObjectMapper();
		Message msg = om.readValue(message.getPayload(), Message.class);
		
		for(WebSocketSession onUser: onUser) {
			onUser.sendMessage(new TextMessage(msg.getName()+": "+msg.getMessage()));
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info("afterConnectionClosed called ");
		onUser.remove(session);
	}

	
}
