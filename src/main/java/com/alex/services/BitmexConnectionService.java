package com.alex.services;

import com.alex.services.handlers.BitmexHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.websocket.DeploymentException;
import java.io.IOException;

@Service
public class BitmexConnectionService {
    @Value("${address.bitmex}")
    private String bitmexAdress;
    @Value("${bitmex.wss.instrument}")
    private String instrument;

    @Autowired
    private BitmexProcessingService processingService;
    @Autowired
    private BitmexSessionStorage sessionStorage;

    @Getter
    private WebSocketConnectionManager connectionManager;

    public void connect() throws IOException, DeploymentException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        BitmexHandler handler = new BitmexHandler(sessionStorage, instrument, processingService);
        connectionManager = new WebSocketConnectionManager(client, handler, bitmexAdress);
        connectionManager.start();
    }
}
