package com.hainan.cs.websocket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

//一开始页面打开时建立连接，建立完后就没他的事了，之后就交给handler了
public class ChatHandShake implements HandshakeInterceptor{

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		// TODO Auto-generated method stub
		
	}
	
	//request可以获取页面请求对象的值
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handlar,
			Map<String, Object> map) throws Exception {

		//getParamater是从这ws://localhost:8080/online_qq/ws?username="+name获取的“username”，
		//我就不懂了为啥用getAttribute获取不到${username}显示的值，照理说这应该在request对象中的
		
		System.out.println("用户"+((ServletServerHttpRequest) request).
				getServletRequest().getParameter("username")+"已经建立连接");//这里可以获取前台传递的值
		if(request instanceof ServletServerHttpRequest){
			ServletServerHttpRequest srequest=(ServletServerHttpRequest)request;
			if(srequest.getServletRequest().getParameter("username")!=null){
				//页面传递的值在session中，把它存储到map中传给handler处理
				map.put("username", srequest.getServletRequest().getParameter("username"));
			}else {
				return false;
				}
		}
		return true;
	}

}
