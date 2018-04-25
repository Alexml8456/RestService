package com.alex.services.handlers;

import com.alex.interfaces.SessionStorage;
import com.alex.model.ComponentStatus;
import com.alex.services.GdaxProcessingService;
import com.alex.services.StatusService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GdaxMultiHandler implements WebSocketHandler {

    @Getter
    private List<GdaxHandler> handlers = new ArrayList<>();
    private SessionStorage sessionStorage;
    private StatusService statusService;

    public void addHandler(GdaxHandler handler) {
        handlers.add(handler);
    }

    public GdaxMultiHandler(List<String> tickers, SessionStorage sessionStorage, GdaxProcessingService processingService, StatusService statusService) {
        this.statusService = statusService;
        tickers.forEach(ticker -> this.addHandler(new GdaxHandler(processingService, ticker)));
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Session started");
        sessionStorage.storeSession(session);
        session.setTextMessageSizeLimit(1000000);
        session.setBinaryMessageSizeLimit(1000000);
        for (GdaxHandler handler : handlers) {
            handler.afterConnectionEstablished(session);
        }
        statusService.reportStatus(ComponentStatus.SUCCESS);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        String message = webSocketMessage.getPayload().toString();
        log.debug(message);

        for (GdaxHandler handler : handlers) {
            handler.handleMessage(session, message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.error("Connection closed with status " + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}