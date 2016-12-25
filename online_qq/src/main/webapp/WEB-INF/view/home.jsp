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