package com.alex.services;


import com.alex.services.handlers.GdaxHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.List;

public class GdaxConnectionService {

    @Autowired
    private GdaxSessionStorage sessionStorage;
    @Autowired
    private StatusService statusService;

    @Autowired
    private GdaxProcessingService processingService;

    @Value("${address.gdax}")
    private String gdaxAddress;



    @Getter
    private WebSocketConnectionManager connectionManager;



    public void connect() throws IOException, DeploymentException {
        String instrument;
        instrument = "BTC-USD";

        StandardWebSocketClient client = new StandardWebSocketClient();

        GdaxHandler handler = new GdaxHandler(instrument, sessionStorage, processingService, statusService);
        connectionManager = new WebSocketConnectionManager(client, handler, gdaxAddress);
        connectionManager.start();
    }
}
