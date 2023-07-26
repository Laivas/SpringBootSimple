var socket;
var stompClient;
var url = window.location.pathname;
var chatId = url.substring(url.lastIndexOf('/') + 1);
var messages = document.querySelector('#messages');
var chat = document.querySelector('#chat-content');
var sendMsg = document.querySelector('#sendMsg');



var chatRoom = document.getElementById('chat');

function connect() {
	
	socket = new SockJS("/chatroom");
	stompClient = Stomp.over(socket);
	
	stompClient.reconnect_delay = 5000;
	
	stompClient.connect({}, connecting);
	
}

function connecting() {
	
	stompClient.subscribe("/queue/chat" + "/" + chatId , messageReceived);
	
	$("#chat").load(" #chat");
	
	scroll();
}

function sendMessage() {
	

	if(stompClient) {
		
		var messageSenderName = document.getElementById("messageSenderName").innerText;

		var message = JSON.stringify({'chatId' : chatId , 'sendMessage' : $("#sendMessage").val() , 'messageSenderName' : messageSenderName , 'date' : Date.now()});

 		stompClient.send("/app/chat" + "/" + chatId , {} , message);
 	
 		scroll();
 	
	}
	
}



function messageReceived(json) {

	scroll();
}


function scroll() {

	var loaded = $("#chat").load(" #chat");

	if(loaded) {
		
		document.getElementById("sendMessage").value = "";
		
		setTimeout( function(){ 
			
			$('#chat-content').scrollTop($('#chat-content')[0].scrollHeight);
			
  }  , 100 );

	}

}

window.addEventListener("load", (event) => {
  setTimeout(() => {

    connect();
    

  }, 200);
});

