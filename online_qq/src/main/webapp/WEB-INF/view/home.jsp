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
<script>
	$(document).ready(function(){
		$("#chatwindow").hide();
		$("#searchinformation").hide();
	});
</script>
<div class="container-fluid">
	<div class="row">
		<div class="col-md-2">
		</div>
		<div class="col-md-8">
			<div class="row">
				<!-- 左边用户列表信息 -->
				<div class="col-md-6">
					<!-- 用户名 -->
					<div class="row">
						<div class="col-md-12">
							<h3 style="text-align:center">${username }</h3>
						</div>
					</div>
					<!-- 好友列表和消息列表 -->
					<div class="row">
						<!-- 好友列表控制 -->
						<div class="col-md-6">
							<button type="button" class="btn btn-block btn-success" onclick="friends()">
							Friends List
							</button>
						</div>
						<script>
							function friends(){
								$.ajax({
									url:"${pageContext.request.contextPath}/home/friends",
									type:"POST",
									dataType:"json",
									success:function(data){
										alert(data.reply);
										var content="好友列表";
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
							Message List
							</button>
						</div>
					</div>
					<!--  好友列表和消息显示容器-->
					<div class="row">
						<div class="col-md-12" style="height:500px" >
							<div class="row" id="fcontent">
							</div>
						</div>
					</div>
					<!-- 搜索好友 -->
					<div class="row">
						<div class="col-md-12">
									<div class="form-group">
										<input class="form-control" type="text" id="friendname"/>
									</div>
									<button type="button" class="btn btn-default" onclick="search()">Submit</button>
							</div>
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
				<div class="col-md-6">
				<!-- 聊天信息层 -->
				<div class="row" id="chatwindow">
					<!-- 好友姓名显示 -->
					<div class="row">
						<div class="col-md-12">
							<h4 style="text-align:center" id="showfname"></h4>
						</div>
					</div>
					<!-- 聊天窗口 -->
					<div class="row">
						<div class="col-md-12" style="height:550px" id="chatcontent">
						</div>
					</div>
					<!-- 发送聊天窗口 -->
					<div class="row">
						<div class="col-md-12">
							<!-- 用websocke传消息,不是用form也不是ajax -->
								<div class="form-group">
									<input class="form-control" type="text" id="message"/>
								</div>
								<button type="button" class="btn btn-default" onclick="send()">Submit</button>
						</div>
					</div>
					<script>
						var websocket;
						if('WebSocket' in window){
							websocket=new WebSocket("ws://localhost:8080/online_qq/ws");
						}
						websocket.onopen = function(event) {
							console.log("WebSocket:已连接");
							console.log(event);
						};
						websocket.onmessage = function(event) {
							var data=JSON.parse(event.data);
							console.log("WebSocket:收到一条消息",data);
							$("#chatcontent").append("<p style=\"color:green\">"+data.username+"</p><p style=\"color:white\">"+data.msg+"</p>");
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
							data["to"]=to;
							$("#chatcontent").append("<p style=\"color:red\">"+'${username}'+"</p><p style=\"color:white\">"+message+"</p>");
							websocket.send(JSON.stringify(data));
							}
					</script>
				</div>
				<!-- 搜所显示层 -->
				<div class="row" id="searchinformation">
					
				</div>
				</div>
			</div>
		</div>
		<div class="col-md-2">
		</div>
	</div>
</div>
</body>
</html>