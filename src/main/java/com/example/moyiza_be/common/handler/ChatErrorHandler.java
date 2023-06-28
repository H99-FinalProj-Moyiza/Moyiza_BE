package com.example.moyiza_be.common.handler;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@Getter
@Slf4j
public class ChatErrorHandler extends StompSubProtocolErrorHandler {
    public ChatErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }
}
