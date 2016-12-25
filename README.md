##基于websocket和redis的伪简单QQ设计
###1.先上截图
![](http://i.imgur.com/HxqfhOQ.jpg)

![](http://i.imgur.com/FR50BDt.jpg)

###2.环境要求
首先，websocket要求是spring4.0以上，网上你找spring redis的例子的话，它配的是spring3.0，要是你把spring-websocket和spring-redis-data结合的话要是版本不一致会出问题。
主要是spring-bean，spring-context，与spring-websocket,spring-redis-data匹配问题造成的。

pom.xml

	<project xmlns="http://maven.apache.org/POM/4.0.0" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>org.springframework.samples.service.service</groupId>
	  <artifactId>online_qq</artifactId>
	  <version>0.0.1-SNAPSHOT</version>
	  <packaging>war</packaging>
	  
	    <properties>
	
			<!-- Generic properties -->
			<java.version>1.6</java.version>
			<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
			<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
			
			<!-- Web -->
			<jsp.version>2.2</jsp.version>
			<jstl.version>1.2</jstl.version>
			<servlet.version>2.5</servlet.version>
			<!-- Spring -->
			<spring-framework.version>3.2.3.RELEASE</spring-framework.version>
	
			<!-- Hibernate / JPA -->
			<hibernate.version>4.2.1.Final</hibernate.version>
	
			<!-- Logging -->
			<logback.version>1.0.13</logback.version>
			<slf4j.version>1.7.5</slf4j.version>
	
			<!-- Test -->
			<junit.version>4.11</junit.version>
	
		</properties>
		
		<dependencies>
		<!-- cofig spring jar -->
			<dependency>  
				<groupId>org.springframework</groupId>  
				<artifactId>spring-core</artifactId>  
				<version>4.0.6.RELEASE</version>  
			</dependency>  
	  
			<dependency>  
				<groupId>org.springframework</groupId>  
				<artifactId>spring-expression</artifactId>  
				<version>4.0.6.RELEASE</version>  
			</dependency>  
	  
			<dependency>  
				<groupId>org.springframework</groupId>  
				<artifactId>spring-beans</artifactId>  
				<version>4.0.6.RELEASE</version>  
			</dependency>  
	  
			<dependency>  
				<groupId>org.springframework</groupId>  
				<artifactId>spring-aop</artifactId>  
				<version>4.0.6.RELEASE</version>  
			</dependency>  
	  
	  <!-- Spring MVC -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-webmvc</artifactId>
				<version>4.0.6.RELEASE</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-messaging</artifactId>
				<version>4.0.6.RELEASE</version>
			</dependency>
			
			<!-- web socket -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-websocket</artifactId>
				<version>4.0.6.RELEASE</version>
			</dependency>
			
	  
			<dependency>  
				<groupId>org.springframework</groupId>  
				<artifactId>spring-context</artifactId>  
				<version>4.0.6.RELEASE</version>  
			</dependency>  
	  
			<dependency>  
				<groupId>org.springframework</groupId>  
				<artifactId>spring-context-support</artifactId>  
				<version>4.0.6.RELEASE</version>  
			</dependency>  
	
			<!-- Spring redis -->
			 <dependency>  
		        <groupId>org.springframework.data</groupId>  
		        <artifactId>spring-data-redis</artifactId>  
		        <version>1.6.6.RELEASE</version>  
	       </dependency> 
			
			<!-- Other Web dependencies -->
			<dependency>
				<groupId>javax.servlet</groupId>
				<artifactId>jstl</artifactId>
				<version>${jstl.version}</version>
			</dependency>
			<dependency>
				<groupId>javax.servlet</groupId>
				<artifactId>servlet-api</artifactId>
				<version>${servlet.version}</version>
				<scope>provided</scope>
			</dependency>
			<dependency>
				<groupId>javax.servlet.jsp</groupId>
				<artifactId>jsp-api</artifactId>
				<version>${jsp.version}</version>
				<scope>provided</scope>
			</dependency>
		
			<!-- Spring and Transactions -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-tx</artifactId>
				<version>${spring-framework.version}</version>
			</dependency>
	
			<!-- Logging with SLF4J & LogBack -->
			<dependency>
				<groupId>org.slf4j</groupId>
				<artifactId>slf4j-api</artifactId>
				<version>${slf4j.version}</version>
				<scope>compile</scope>
			</dependency>
			<dependency>
				<groupId>ch.qos.logback</groupId>
				<artifactId>logback-classic</artifactId>
				<version>${logback.version}</version>
				<scope>runtime</scope>
			</dependency>
	
	
			<!-- https://mvnrepository.com/artifact/commons-pool/commons-pool -->
	<dependency>
	    <groupId>org.apache.commons</groupId>
			   <artifactId>commons-pool2</artifactId>
		<version>2.4.2</version>
	</dependency>
			
			<!-- Test Artifacts -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-test</artifactId>
				<version>${spring-framework.version}</version>
				<scope>test</scope>
			</dependency>
			<dependency>
				<groupId>junit</groupId>
				<artifactId>junit</artifactId>
				<version>${junit.version}</version>
				<scope>test</scope>
			</dependency>
		<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
		<dependency>
		    <groupId>redis.clients</groupId>
		    <artifactId>jedis</artifactId>
		    <version>2.8.0</version>
		</dependency>
	
		<!-- jackson spring通过response向前台发送数据是转成json格式要用到-->
	        <dependency>  
	            <groupId>org.codehaus.jackson</groupId>  
	            <artifactId>jackson-mapper-asl</artifactId>  
	            <version>1.9.13</version>  
	        </dependency>  
	        <dependency>  
	            <groupId>org.codehaus.jackson</groupId>  
	            <artifactId>jackson-core-asl</artifactId>  
	            <version>1.9.13</version>  
	        </dependency> 
	        <dependency>  
	            <groupId>com.fasterxml.jackson.core</groupId>  
	            <artifactId>jackson-databind</artifactId>  
	            <version>2.5.0</version>  
	        </dependency> 
	     	<!-- http://mvnrepository.com/artifact/org.json/json -->
			<dependency>
				<groupId>org.json</groupId>
				<artifactId>json</artifactId>
				<version>20160212</version>
			</dependency>
	<!-- https://mvnrepository.com/artifact/net.sf.json-lib/json-lib -->
	<dependency>
	    <groupId>net.sf.json-lib</groupId>
	    <artifactId>json-lib</artifactId>
	    <version>2.4</version>
	     <classifier>jdk15</classifier> 
	</dependency>
	
		</dependencies>	
	<build>  
	    <plugins>  
	        <plugin>  
	            <groupId>org.apache.maven.plugins</groupId>  
	            <artifactId>maven-compiler-plugin</artifactId>  
	            <version>2.0.2</version>  
	            <configuration>  
	                <source>1.7</source>  
	                <target>1.7</target> 
	                <encoding>utf8</encoding> 
	            </configuration>  
	        </plugin>  
	    </plugins>  
	</build>  
	</project>

依赖的插件截图

![](http://i.imgur.com/3s70C7o.jpg)

spring的application-config.xml配置

	<?xml version="1.0" encoding="UTF-8"?>
	
	<beans 
		xmlns="http://www.springframework.org/schema/beans"  
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	    xmlns:p="http://www.springframework.org/schema/p"  
	    xmlns:context="http://www.springframework.org/schema/context"  
	    xmlns:jee="http://www.springframework.org/schema/jee" 
	    xmlns:tx="http://www.springframework.org/schema/tx"  
	    xmlns:aop="http://www.springframework.org/schema/aop"  
	    xsi:schemaLocation="  
	            http://www.springframework.org/schema/beans 
	            http://www.springframework.org/schema/beans/spring-beans.xsd  
	            http://www.springframework.org/schema/context 
	            http://www.springframework.org/schema/context/spring-context.xsd">  
	            
	     <context:component-scan base-package="com.hainan.cs"/>  
	<context:property-placeholder location="classpath:redis.properties" />  
	<bean id="poolConfig" class="redis.clients.jedis.JedisPoolConfig">  
	        <property name="maxIdle" value="${redis.maxIdle}" />  
	        <property name="testOnBorrow" value="${redis.testOnBorrow}" />  
	    </bean>  
	      
	    <bean id="connectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory"  
	        p:host-name="${redis.host}" p:port="${redis.port}"   p:pool-config-ref="poolConfig" p:password="${redis.pass}"/>  
	      
	    <bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">  
	        <property name="connectionFactory"   ref="connectionFactory" />  
	    </bean>     
	    <bean id="userDao" class="com.hainan.cs.dao.UserDaoImp"></bean>  
	</beans>

redis连接设置redis.properties,redis有桌面编辑软件,redis desktop manager

    # Redis settings  
    redis.host=127.0.0.1
    redis.port=6379  
    redis.pass=
    redis.maxIdle=300  
    redis.maxActive=600  
    redis.maxWait=1000  
    redis.testOnBorrow=true  


springmvc配置 mvc-config.xml配置

	<?xml version="1.0" encoding="UTF-8"?>
	
	<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:context="http://www.springframework.org/schema/context"
		xsi:schemaLocation="http://www.springframework.org/schema/beans   
	        http://www.springframework.org/schema/beans/spring-beans.xsd   
	        http://www.springframework.org/schema/context   
	        http://www.springframework.org/schema/context/spring-context.xsd   
	        http://www.springframework.org/schema/mvc   
	        http://www.springframework.org/schema/mvc/spring-mvc.xsd  ">
	
	     <context:component-scan base-package="com.hainan.cs"/>  
	            
		<!-- 让springmvc放过存放静态资源的请求 -->
		<mvc:resources mapping="/resources/**" location="/resources/" />
	
	    <mvc:annotation-driven />
	
		<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		        <!-- Example: a logical view name of 'showMessage' is mapped to '/WEB-INF/jsp/showMessage.jsp' -->
		        <property name="prefix" value="/WEB-INF/view/"/>
		        <property name="suffix" value=".jsp"/>
		</bean>
	
	</beans>

web.xml配置

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
		xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
		version="3.0">
	
	    <display-name>online_qq</display-name>
	    
	   <!--
			- Location of the XML file that defines the root application context.
			- Applied by ContextLoaderListener.
		-->
	    <context-param>
	        <param-name>contextConfigLocation</param-name>
	        <param-value>classpath:spring/application-config.xml</param-value>
	    </context-param>
	
	    <listener>
	        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	    </listener>
	    
	    
	    <!--
			- Servlet that dispatches request to registered handlers (Controller implementations).
		-->
	    <servlet>
	        <servlet-name>dispatcherServlet</servlet-name>
	        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	        <init-param>
	            <param-name>contextConfigLocation</param-name>
	            <param-value>/WEB-INF/mvc-config.xml</param-value>
	        </init-param>
	        <load-on-startup>1</load-on-startup>
	    </servlet>
	
	    <servlet-mapping>
	        <servlet-name>dispatcherServlet</servlet-name>
	        <url-pattern>/</url-pattern>
	    </servlet-mapping>
	
	</web-app>


###3.redis操作部分
redis操作部分主要管用户的注册，登陆，好友搜索，好友添加等

RedisGeneratorDao.java

	package com.hainan.cs.dao;
	
	import java.io.Serializable;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.RedisSerializer;
	
	public class RedisGeneratorDao<k extends Serializable, v extends Serializable> {
	
		@Autowired
		protected RedisTemplate<k,v> redisTemplate;
		
		public void setRedisTemplate(RedisTemplate<k,v> redisTemplate){
			this.redisTemplate=redisTemplate;
		}
		protected RedisSerializer<String> getRedisSerializer(){
			return redisTemplate.getStringSerializer();
		}
	}
@Autowired 和@Resource都是依赖注入的方式，当然也可以使用

    ClassPathXmlApplicationContext context=new ClassPaxthXmlApplicationContext("application-config.xml");
    UserDaoImp udi=context.getBean(UserDaoImp.class);

来获取spring容器中的对象。

UserDaoImp.java

	package com.hainan.cs.dao;
	
	import java.util.List;
	import java.util.Map;
	import java.util.UUID;
	
	import org.springframework.context.support.ClassPathXmlApplicationContext;
	import org.springframework.dao.DataAccessException;
	import org.springframework.data.redis.connection.RedisConnection;
	import org.springframework.data.redis.core.BoundHashOperations;
	import org.springframework.data.redis.core.BoundListOperations;
	import org.springframework.data.redis.core.RedisCallback;
	import org.springframework.data.redis.serializer.RedisSerializer;
	import org.springframework.stereotype.Repository;
	
	import com.hainan.cs.bean.User;
	@Repository(value="userDao")
	public class UserDaoImp extends RedisGeneratorDao<String,String> implements UserDao{
		//添加user Hash hsetNX
		@Override
		public boolean addUser(final User user){
			boolean result=redisTemplate.execute(new RedisCallback<Boolean>(){
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					RedisSerializer<String> serializer=getRedisSerializer();
					byte[] key=serializer.serialize(user.getId());
					byte[] name=serializer.serialize("name");
					byte[] namea=serializer.serialize(user.getUsername());
					byte[] passworda=serializer.serialize(user.getPassword());
					byte[] password=serializer.serialize("password");
					byte[] email=serializer.serialize("email");
					byte[] emaila=serializer.serialize(user.getEmial());
					byte[] phone=serializer.serialize("phone");
					byte[] phonea=serializer.serialize(user.getPhone());
					byte[] address=serializer.serialize("address");
					byte[] addressa=serializer.serialize(user.getAdress());
					connection.hSetNX(key, name,namea);//hash类型的提交<key,hash的key,hash的value>
					connection.hSetNX(key, password, passworda);
					connection.hSetNX(key, email, emaila);
					connection.hSetNX(key, phone, phonea);
					connection.hSetNX(key, address, addressa);
					return true;
				}
			});
			return result;
		}
		//获取用户信息
		public Map<String,String> getUser(String userid){
			BoundHashOperations<String,String,String> bhops=redisTemplate.boundHashOps(userid);
			return bhops.entries();
		}
		//添加用户好友
		@Override
		public void addFriends(final String userid, final String friendid, final String group){
			Boolean result=redisTemplate.execute(new RedisCallback<Boolean>(){
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					RedisSerializer<String> serializer=redisTemplate.getStringSerializer();
					String key="friend"+userid;
					byte[] fkey=serializer.serialize(key);
					byte[] ffriendid=serializer.serialize(friendid);
					byte[] fgroup=serializer.serialize(group);
					Boolean r= connection.hSet(fkey, ffriendid, fgroup);
					return r;
				}
				
			});
		}
		//删除用户好友
		@Override
		public void deleteFriend(User user, String friendsid){
			BoundHashOperations<String,String,String> bhops=redisTemplate.boundHashOps("friend"+user.getId());
			bhops.delete(friendsid);
		}
		//添加用户好友组
		@Override
		public void addFriendsGroup(final User user, final String group){
			redisTemplate.execute(new RedisCallback<Boolean>(){
	
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					RedisSerializer<String> serializer=redisTemplate.getStringSerializer();
					byte[] key=serializer.serialize("group"+user.getId());
					byte[] value=serializer.serialize(group);
					connection.lPush(key, value);
					return true;
				}
				
			});
		}
		//添加用户的列表的hashmap
		@Override
		public void addUserToList(final String key,final String userid,final String username){
			redisTemplate.execute(new RedisCallback<Boolean>(){
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					RedisSerializer<String> serializer=getRedisSerializer();
					byte[] a=serializer.serialize(key);
					byte[] c=serializer.serialize(userid);
					byte[] b=serializer.serialize(username);
					connection.hSet(a, c, b);
					return true;
				}
				});
		}
		//获取用户列表
		public Map<String,String> getUserList(){
			BoundHashOperations<String,String,String> hlops=redisTemplate.boundHashOps("userlist");
			return hlops.entries();
		}
		//删除对象
		@Override
		public void deleteUser(String userid){
			redisTemplate.delete(userid);
		}
		//删除hash中的一条记录
		@Override
		public void deleteUserFromList(String key,String id){
			BoundHashOperations<String,Object,Object> bhops=redisTemplate.boundHashOps(key);
			bhops.delete(id);
		}
		//获取用户的好友分组
		public List<String> getFriendGroups(String userid){
			BoundListOperations<String,String> blops=redisTemplate.boundListOps("group"+userid);
			System.out.println(userid);
			return blops.range(0, blops.size());
		}
		//获取用户的好友列表
		public Map<String,String> getFriends(String userid){
			BoundHashOperations<String,String,String> bhops=redisTemplate.boundHashOps("friend"+userid);
			return bhops.entries();
		}
		
		//测试
		public static void main(String args[]){
			ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("spring/application-config.xml");
			UserDaoImp udi=context.getBean(UserDaoImp.class);
			User u=new User();
			String id=UUID.randomUUID().toString();//自动生成一个id
			u.setId(id);
			u.setUsername("zhangsan");
			u.setPassword("123456");
			u.setEmial("test@qq.com");
			u.setPhone("234556");
			u.setAdress("china haikou");
			udi.addUser(u);
			udi.addUserToList("userlist", id, "zhangsan");
			context.close();
		}
	}

###4.websocket通信部分
主要管，用户向指定好用发送消息，消息之间的同步

ChatConfig.java

	package com.hainan.cs.websocket;	
	import javax.annotation.Resource;	
	import org.springframework.stereotype.Component;	
	import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;	
	import org.springframework.web.socket.config.annotation.EnableWebSocket;	
	import org.springframework.web.socket.config.annotation.WebSocketConfigurer;	
	import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;	
		
	@Component	
	@EnableWebSocket	
	public class ChatConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{	
		@Resource	
		private ChatHandler handler;	
		@Override	
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	
			// TODO Auto-generated method stub	
			registry.addHandler(handler, "/ws").addInterceptors(new ChatHandShake());	
		}
	}

ChatHandler.java

	package com.hainan.cs.websocket;	
	import java.util.HashMap;	
	import java.util.Map;	
		
	import org.springframework.stereotype.Component;	
	import org.springframework.web.socket.CloseStatus;	
	import org.springframework.web.socket.WebSocketHandler;	
	import org.springframework.web.socket.WebSocketMessage;	
	import org.springframework.web.socket.WebSocketSession;	
		
	import net.sf.json.JSONObject;	
		
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
		//再handshake建立连接后，username存到map中了，wsession怎么拿到的map的值	
		@Override	
		public void afterConnectionEstablished(WebSocketSession wsession) throws Exception {	
			String uname=(String) wsession.getAttributes().get("username");	
			System.out.println("用户"+uname+"已经加入");	
			if(usermap.get(uname)==null){	
				usermap.put(uname, wsession);	
			}	
		}	
			
		// message 是前台传过来的json数据，包括用户名，和发送的消息，要将消息转发给所有的其他用户,重新转发的话message格式和原来是一样的	
		@Override	
		public void handleMessage(WebSocketSession wsession, WebSocketMessage<?> message) throws Exception {	
			//TextMessage tm=new TextMessage(message.toString());	
			System.out.println("开始发送消息"+usermap.size());	
			//这是json字符串	
			String jm=message.getPayload().toString();	
			JSONObject json=JSONObject.fromObject(jm);	
			System.out.println(jm);	
			System.out.println(json.get("to"));	
			for(Map.Entry<String,WebSocketSession>entry:usermap.entrySet()){	
				String key=entry.getKey();	
				WebSocketSession value=entry.getValue();	
				//给指定的好友发消息	
				if(key.equals(json.get("to"))){	
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

ChatHandShake.java

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

home.jsp

	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="${pageContext.request.contextPath}/resources/layoutit/src/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/layoutit/src/css/style.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/resources/layoutit/src/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/layoutit/src/js/bootstrap.min.js"></script>
	<title>Online QQ</title>
	</head>
	<body>
	<script type="text/javascript">
							var name="${username}";
							var websocket;
							if('WebSocket' in window){
								websocket=new WebSocket("ws://localhost:8080/online_qq/ws?username="+name);
							}
							websocket.onopen = function(event) {
								console.log("WebSocket:已连接");
								console.log(event);
							};
							websocket.onmessage = function(event) {
								var data=JSON.parse(event.data);
								console.log("WebSocket:收到一条消息",data);
								$("#chatcontent").append("<p style=\"color:green\">"+data.username+":</p><p style=\"color:black\">"+data.msg+"</p>");
							};
							websocket.onerror = function(event) {
								console.log("WebSocket:发生错误 ");
								console.log(event);
							};
							websocket.onclose = function(event) {
								console.log("WebSocket:已关闭");
								console.log(event);
							}
							function send(){
								var message=$("#message").val();
								var data={};
								data["username"]="${username}";
								data["msg"]=message;
								//to是发送消息的目标
								data["to"]=to;
								$("#chatcontent").append("<p style=\"color:red\">"+'${username}'+":</p><p style=\"color:black\">"+message+"</p>");
								websocket.send(JSON.stringify(data));
								}
	</script>
	<script>
		$(document).ready(function(){
			$("#chatwindow").hide();
			$("#searchinformation").hide();
		});
	</script>
	<div class="container-fluid">
		<div class="row" style="background-color:#22DDDD">
			<div class="col-md-2">
			</div>
			<div class="col-md-8" style="margin-top:50px">
				<div class="row">
					<!-- 左边用户列表信息 -->
					<div class="col-md-3">
						<!-- 用户名 -->
						<div class="row">
							<div class="col-md-12" style="height:150px ;  background-color:#F7F7B3">
								<h3 style="text-align:center">${username }</h3>
							</div>
						</div>
						<!-- 好友列表和消息列表 -->
						<div class="row" style="background-color:#EAF3EA">
							<!-- 好友列表控制 -->
							<br>
							<div class="col-md-6">
								<button type="button" class="btn btn-block btn-success" onclick="friends()">
								好友列表
								</button>
							</div>
							<script>
								function friends(){
									$.ajax({
										url:"${pageContext.request.contextPath}/home/friends",
										type:"POST",
										dataType:"json",
										success:function(data){
											//alert(data.reply);
											var content="";
											var group=data.group;//这是一个好友分组的列表
											var friends=data.friends;//这是所有好友的map<好友的姓名，好友的分组>
											for(var i=0;i<group.length;i++){
												content=content+"<h4 style=\"color:red\">"+group[i]+"</h4>";
												for(var key in friends){
													if(friends[key]==group[i]){
														content=content+"<p><button class=\"btn btn-block\" value=\""+key+"\" onclick=\"chat(this.value)\">"+key+"</button></p>";
														//content=content+"<p><button class=\"btn btn-block\" onclick=\"chat("+key+")\">"+key+"</button></p>";
														//这种是错误的
													}
												}
											}
											$("#fcontent").html(content);
										},
										error:function(data){
											alert("error");
										}
									});
								}
								var to;
								function chat(key){
									to=key;
									$("#chatwindow").show();
									$("#searchinformation").hide();
									$("#showfname").html(key);
								}
							</script>
							<!-- 消息列表控制 -->
							<div class="col-md-6">
								<button type="button" class="btn btn-block btn-success">
								消息列表
								</button>
							</div>
						</div>
						<!--  好友列表和消息显示容器-->
						<div class="row" style="height:596px;background-color:#EAF3EA" >
							<div class="col-md-12" >
								<div class="row" id="fcontent" style="margin-left:7px;margin-right:7px">
								</div>
							</div>
						</div>
						<!-- 搜索好友 -->
						<div class="row" style="background-color:#EAF3EA">
							<div class="col-md-12">
										<div class="col-sm-8">
											<input class="form-control" type="text" id="friendname"/>
										</div>
										<div class="col-sm-4">
										<button type="button" class="btn btn-default" onclick="search()">搜索好友</button>
										</div>
								</div>
								<br><br><br><br>
						</div>
						<script>
							function search(){
								$.ajax({
									data:"friendname="+$("#friendname").val(),
									dataType:"json",
									url:"${pageContext.request.contextPath}/home/search",
									type:"POST",
									success:function(data){
										if(data.result=="exist"){
											var content="姓名："+data.name+"邮箱："+data.email+"电话："+data.phone+"地址："
											+data.address+"<a class=\"btn btn-primary\" href=\"${pageContext.request.contextPath}/home/addfriend?friendname="+data.name+"\">添加好友</a>";
											$("#searchinformation").show();
											$("#chatwindow").hide();
											$("#searchinformation").html(content);
										}
										if(data.result=="notexist"){
											$("#searchinformation").show();
											$("#chatwindow").hide();
											$("#searchinformation").html("no such user name.");
										}
									},
									error:function(data){
										alert("error");
									}
								});
							}
						</script>
					</div>
					<!-- 右边聊天窗口信息 -->
					<div class="col-md-9">
					<!-- 聊天信息层 -->
					<div class="row" id="chatwindow" style="background-color:#D6F5DC">
						<!-- 好友姓名显示 -->
						<div class="row" >
							<div class="col-md-12">
								<h4 style="text-align:center" id="showfname"></h4>
								<hr color="balck"/>
							</div>
						</div>
						<!-- 聊天窗口 -->
						<div class="row" >
							<div class="col-md-12" style="height:750px;margin-left:30px" id="chatcontent">
							</div>
						</div>
						<!-- 发送聊天窗口 -->
						<div class="row">
							<div class="col-md-12">
								<!-- 用websocke传消息,不是用form也不是ajax -->
									<div class="col-sm-9">
										<input class="form-control" type="text" id="message"/>
									</div>
									<div class="col-sm-3">
									<button type="button" class="btn btn-default" onclick="send()">提交</button>
									</div>
							</div>
							<br><br><br>
						</div>
						
					</div>
					<!-- 搜所显示层 -->
					<div class="row" style="background-color:#D6F5DC">
						<div class="row" id="searchinformation"  style="margin-left:50px;margin-top:50px">
						</div>
					</div>
					</div>
				</div>
			</div>
			<div class="col-md-2">
			</div>
				<div class="col-md-12">
				<br><br>
				<p class="text-center">
					copyright ©UML Analyzer  2015 ICP 15003200 <a href="http://www.hainu.edu.cn/">Hainan University</a>
					|  Designed by Huang Liang
				</p>
			</div>
		</div>
	
	</div>
	
	</body>
	</html>

###5.代码地址

https://github.com/huangliang992/online_qq