package com.hainan.cs.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ChatHandler implements WebSocketHandler{
	
	private  Map<String, WebSocketSession> usermap=new HashMap<String, WebSocketSession>();
	
	//连接关闭后的操作
	@Override
	public void afterConnectionClosed(WebSocketSession wsession, CloseStatus status) throws Exception {
		System.out.println("用户"+wsession.getAttributes().get("username")+"已经关闭");
		for(Map.Entry<String, WebSocketSession>entry:usermap.entrySet()){
			String key=entry.getKey();
			WebSocketSession value=entry.getValue();
			if(key.equals(wsession.getAttributes().get("username"))){
				usermap.remove(key);
				System.out.println("用户"+key+"已经移除");
			}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession wsession) throws Exception {
		String uname=(String) wsession.getAttributes().get("username");
		System.out.println("用户"+uname+"已经加入");
		if(usermap.get(uname)==null){
			usermap.put(uname, wsession);
		}
	}
	
	// message 是前台传过来的json数据，包括用户名，和发送的消息，要将消息转发给所有的其他用户
	@Override
	public void handleMessage(WebSocketSession wsession, WebSocketMessage<?> message) throws Exception {
		//TextMessage tm=new TextMessage(message.toString());
		System.out.println("开始发送消息"+usermap.size());
		for(Map.Entry<String,WebSocketSession>entry:usermap.entrySet()){
			String key=entry.getKey();
			WebSocketSession value=entry.getValue();
			if(key!=wsession.getAttributes().get("username")){
				if(value!=null&&value.isOpen()){
					value.sendMessage(message);
				}
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession wsession, Throwable th) throws Exception {
		if(wsession.isOpen()){
			wsession.close();
		}
		for(Map.Entry<String, WebSocketSession>entry:usermap.entrySet()){
			String key=entry.getKey();
			WebSocketSession value=entry.getValue();
			if(key.equals(wsession.getAttributes().get("username"))){
				usermap.remove(key);
				System.out.println("传输出错！用户"+key+"已经移除");
			}
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
