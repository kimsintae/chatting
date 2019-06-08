<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="resources/sockjs.min.js"></script>
</head>
<body>
	<h1>
		Hello world!  
	</h1>
	
	<form id="chatForm">
		<input type="text" id="message"/>
		<button>send</button>
	</form>
	<div id="chat"></div>
	<script>
		$(document).ready(function(){
			$("#chatForm").submit(function(event){
				event.preventDefault();
				send();
				$("#message").val('').focus();
			});
		});
		
		var sock = new SockJS("/chat");
		
		// websocket 으로부터 받은 메세지를 처리
		sock.onmessage = function(e){
			console.log("onmessage = "+e.data);
			$("#chat").append(e.data + "<br/>");
		}
		
		sock.onclose = function(){
			$("#chat").append("connetion exit");
		}
		
		sock.onopen = function(){
			$("#chat").append("connetion open !");
		}
		
		// websocket 으로 메세지(json) 보내기
		function send(){
			var json = '{"name":"kim","message":"'+$("#message").val()+'"}';
			sock.send(json);
		}
		
		
	</script>
</body>
</html>
