package com.alex.services;

import com.alex.interfaces.SessionStorage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class GdaxSessionStorage implements SessionStorage<WebSocketSession> {

    private WebSocketSession session;

    @Override
    public void storeSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public WebSocketSession getSession() {
        return session;
    }
}
