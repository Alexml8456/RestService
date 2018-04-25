package com.alex.services;


import com.alex.services.handlers.GdaxHandler;
import com.alex.services.handlers.GdaxMultiHandler;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
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
        List<String> instruments;
        instruments = Arrays.asList("BTC-USD");

        StandardWebSocketClient client = new StandardWebSocketClient();

        GdaxMultiHandler handler = new GdaxMultiHandler(instruments, sessionStorage, processingService, statusService);
        connectionManager = new WebSocketConnectionManager(client, handler, gdaxAddress);
        connectionManager.start();
    }
}
