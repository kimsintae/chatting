package com.sintae.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sintae.vo.Message;

public class RoomHandler extends TextWebSocketHandler{
	
	private List<WebSocketSession> roomList = new ArrayList<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("afterConnectionEstablished called ");
		System.out.println(roomList.size());
		roomList.add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("handleTextMessage called : "+message.getPayload());
		
		//브라우저로부터 json 메세지를 받아서 파싱
		ObjectMapper om = new ObjectMapper();
		Message msg = om.readValue(message.getPayload(), Message.class);
		
		
		// 연결된 유저들로부터 온 텍스트들을 보냄
		for(WebSocketSession room: roomList) {
			room.sendMessage(new TextMessage(msg.getName()+": "+msg.getMessage()));
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("afterConnectionClosed called ");
		roomList.remove(session);
	}

	
}
