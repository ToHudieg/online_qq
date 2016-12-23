package com.hainan.cs.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class ChatHandShake implements HandshakeInterceptor{

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handlar,
			Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("用户"+((ServletServerHttpRequest) request).
				getServletRequest().getSession(false).getAttribute("username")+"已经建立连接");//这里可以获取前台传递的值
		if(request instanceof ServletServerHttpRequest){
			ServletServerHttpRequest srequest=(ServletServerHttpRequest)request;
			HttpSession session=srequest.getServletRequest().getSession(false);
			if(session.getAttribute("username")!=null){
				map.put("username", session.getAttribute("username"));
			}else {
				return false;
				}
		}
		return true;
	}

}
