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
											content=content+"<h3>"+group[i]+"</h3>";
											for(var key in friends){
												if(friends[key]==group[i]){
													content=content+"<p>"+key+"</p>";
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
								<form class="form" >
									<div class="form-group">
										<input class="form-control" type="text" />
									</div>
									<button type="submit" class="btn btn-default">Submit</button>
								</form>
							</div>
					</div>
				</div>
				<!-- 右边聊天窗口信息 -->
				<div class="col-md-6">
					<!-- 好友姓名显示 -->
					<div class="row">
						<div class="col-md-12">
							<h4 style="text-align:center">${friendname }</h4>
						</div>
					</div>
					<!-- 聊天窗口 -->
					<div class="row">
						<div class="col-md-12" style="height:550px">
						</div>
					</div>
					<!-- 发送聊天窗口 -->
					<div class="row">
						<div class="col-md-12">
							<form class="form" >
									<div class="form-group">
										<input class="form-control" type="text" />
									</div>
									<button type="submit" class="btn btn-default">Submit</button>
								</form>
						</div>
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