package com.alex.services.handlers;

import com.alex.interfaces.SessionStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

@Slf4j
public class BitmexHandler implements WebSocketHandler {
    private SessionStorage sessionStorage;

    public BitmexHandler(SessionStorage sessionStorage){
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Session started");
        sessionStorage.storeSession(session);
        session.setTextMessageSizeLimit(1000000);
        session.setBinaryMessageSizeLimit(1000000);
        session.sendMessage(new TextMessage("{\"op\": \"subscribe\", \"args\": [\"trade:XBTUSD\"]}"));
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        String message = webSocketMessage.getPayload().toString();
        log.debug(message);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.error("Connection closed with status " + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
