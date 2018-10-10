<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>登录</title>
<meta name="decorator" content="blank" />
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/static/css/tools.css" />
<script src="/static/js/jquery-1.9.1.js" type="text/javascript"></script>

<script type="text/javascript">
	function submitButton(){
		if($("#username").val() != "admin" || $("#password").val() != "admin"){
	    	$("#message").css("display","block");
	    }else{
	    	$("#message").css("display","none");
	    	$("#loginForm").submit();
	    }
	}

</script>
</head>
<body>
	<form id="loginForm" class="form-signin" action="/parameter/load" method="post">
		<label class="input-label" for="username">登录名：</label> 
		<input type="text" id="username" name="username" class="input-block-level required" > 
		<br/>
		<label class="input-label" for="password">密    码：</label> 
		<input type="password" id="password" name="password" class="input-block-level required">
		<label id="message" style="display:none; color:red; ">用户名和密码错误，请重新输入！</label>
		<br/>
		<input class="btn btn-large btn-primary" style="margin-left:110px;" type="button" value="登 录" onclick="submitButton();" />
	</form>
	<div class="footer">
		Copyright &copy; 2018  <a href="http://www.soundhw.com/">桑德新环卫投资有限公司</a>	- Powered By  1.0
	</div>
</body>
</html>