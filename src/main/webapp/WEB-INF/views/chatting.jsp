<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>채팅방</title>
    <script src="resources/sockjs.min.js"></script>
 	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	
    <style>
        #message{
            width: 90%;
            height: 100%;
            padding: 10px;
        }
        #snedBtn{
            width: 9%;
            height: 100%;
        }
        #chatBody,#userList,#chatRoomList{
            padding: 15px;
        }
        #chatBody,#msgBox{
            margin-right:10px;
            margin-left: 10px;
        }
        #userList,#chatRoomList{
            text-align: center;
        }
        .userName{
           font-weight: bold;
        }
        .msgPart{
            height: 50px;
            
        }
    </style>
</head>
<body>
<h2 class="text-primary" style="margin: 20px;">Chatting v0.1</h2>
<div class="row">
    <div id="userList" class="bg-info col-sm-2" style="height: 600px;">
        <h6>접속자 목록 </h6>
    </div>
    <div id="chatBody" class="bg-info col-sm-8" style="height: 600px; overflow-y: scroll">
    </div>
    <div id="chatRoomList" class="bg-info col-sm" style="height: 600px;">
        <h6>채팅방 목록</h6>
    </div>
    <div class="col-sm-2"></div>
    <div id="msgBox" class="bg-info col-sm-8" style="height: 50px; padding: 0">
    	<form id="chatForm">
	        <input id="message" type="text" name="message">
	        <button id="snedBtn" class="btn btn-success">전송</button>
        </form>
    </div>
</div>
 <script>
 
 /*  
 	- template - 
 	'{"type":"????","name":"${user.name}","message":"'+$("#message").val()+'"}'
 */
 
	$(document).ready(function(){
		$("#chatForm").submit(function(event){
			event.preventDefault();
			send('{"type":"msg","name":"${user.name}","message":"'+$("#message").val()+'"}');
			$("#message").val('').focus();
		});
	});
	
	var sock = new SockJS("/chat");
	// websocket 으로부터 받은 메세지를 처리
	sock.onmessage = function(e){
		console.log("onmessage = "+e.data);
		$("#chatBody").append(e.data + "<br/>");
	}
	
	// 퇴장했을때 호출되는 함수
	sock.onclose = function(){
		$("#chatBody").append("connetion exit");
		send('{"type":"exit","name":"${user.name}","message":"'+$("#message").val()+'"}');
	}
	
	// 입장했을 때 호출되는 함수
	sock.onopen = function(){
		var html = "<div class=\"userName\">${user.name}</div>";
		$("#userList").append(html);
		send('{"type":"enter","name":"${user.name}","message":"'+$("#message").val()+'"}');
	}
	
	// websocket 으로 메세지(json) 보내기
	function send(msg){
		sock.send(msg);
	}

	
</script>   
</body>
</html>