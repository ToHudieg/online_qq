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
<script src="${pageContext.request.contextPath}/resources/layoutit/src/js/scripts.js"></script>
<script src="${pageContext.request.contextPath}/resources/jquery_validation/lib/jquery.js"></script>
<script src="${pageContext.request.contextPath}/resources/jquery_validation/dist/jquery.validate.min.js"></script>
<title>Sign Up</title>
</head>
<style>
.error{
color:red;
	}
</style>
<body>
<script type="text/javascript">
function check(){
	$.ajax({
		data:"username="+$("#username").val(),
		dataType:'json',
		type:"GET",
		url:"${pageContext.request.contextPath}/sign/check",
		success:function(data){
			$("#userlabel").html("user name:<span style=\"color:red\">"+data.msg+"</span>");
			},
		error:function(data){
			alert("error");
			}
		});
}
</script>
<div class="container-fluid">
	<div class="row" style="background-color:#39D8D8;height:768px">
		<div class="col-md-12">
			<div class="row">
				<div class="col-md-4">
				</div>
				<div class="col-md-4" style="background-color:#f5f5f5;margin-top:80px">
				<br><br>
					<h2 style="text-align:center">注册</h2>
					<br><br>
					<form role="form" id="signup" action="${pageContext.request.contextPath}/sign/adduser">
						<div class="form-group">
							 
							<label for="username" id="userlabel">
								用户名:
							</label>
							<input type="text" class="form-control" id="username" name="username" onBlur="check()"/>
						</div>
						<div class="form-group">
							 
							<label for="password">
								密码:
							</label>
							<input type="password" class="form-control" id="password" name="password"/>
						</div>
						<div class="form-group">
							 
							<label for="rpassword">
								确认密码:
							</label>
							<input type="password" class="form-control" id="rpassword" name="rpassword"/>
						</div>
						<div class="form-group">
							 
							<label for="email">
								邮箱:
							</label>
							<input type="email" class="form-control" id="email" name="email"/>
						</div>
						<div class="form-group">
							 
							<label for="phone">
								电话:
							</label>
							<input type="text" class="form-control" id="phone" name="phone"/>
						</div>
						<div class="form-group">
							 
							<label for="address">
								地址:
							</label>
							<input type="text" class="form-control" id="address" name="address"/>
						</div>
						<button type="submit" class="btn btn-default" id="btn1">
							提交
						</button>
						<br><br>
					</form>
					<script type="text/javascript">
						$("#signup").validate({
							rules:{
									username: "required",
									password: "required",
									rpassword: {
										required: true,
										equalTo: "#password",
										},
									email: "required",
									phone: "required"
									},
									
							errorPlacement: function(error, element) {
							// Append error within linked label
								$( element )
									.closest( "form" )
										.find( "label[for='" + element.attr( "id" ) + "']" )
											.append( error );
								},
							errorElement: "span",
							messages:{
									password:"please input password",
									rpassword:{
										required:"please input password",
										equalTo: "password not same"
										}
								}
						});
					</script>
				
				</div>
				<div class="col-md-4">
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>