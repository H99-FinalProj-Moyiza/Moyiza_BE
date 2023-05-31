var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function setConnected2(connected) {
    $("#connect2").prop("disabled", connected);
    $("#disconnect2").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('http://43.200.169.48/chat/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chat/clubchat/1', function (greeting) {
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function connect2() {
    var socket = new SockJS('http://43.200.169.48/chat/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected2(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chat/onedaychat/1', function (greeting) {
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function disconnect2() {
    if (stompClient !== null) {
    }
    setConnected2(false);
    stompClient.disconnect()
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/send/clubchat/1", {}, JSON.stringify({
        'senderNickname': $("#name").val(),
        'content' : 'tempclubchat1'
    }));
}

function sendName2() {
    stompClient.send("/app/send/onedaychat/1", {}, JSON.stringify({
        'senderNickname': $("#name2").val(),
        'content' : 'temponedaychat1'
    }));
}

function showGreeting(message) {

    $("#greetings").append("<tr><td>" + message.content + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#connect2" ).click(function() { connect2(); });
    $( "#disconnect2" ).click(function() { disconnect2(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#send2" ).click(function() { sendName2(); });
});