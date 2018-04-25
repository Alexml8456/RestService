package com.alex.services.handlers;

import com.alex.interfaces.SessionStorage;
import com.alex.model.ComponentStatus;
import com.alex.services.GdaxProcessingService;
import com.alex.services.StatusService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GdaxHandler {

    private String ticker;
    private GdaxProcessingService processingService;

    public static final String TRADE_SUBSCRIBE_COMMAND = "{\n" +
            "  \"type\": \"subscribe\",\n" +
            "  \"channels\": [\n" +
            "    {\n" +
            "      \"name\": \"ticker\",\n" +
            "      \"product_ids\": [\n" +
            "        \"{ticker}\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public GdaxHandler(GdaxProcessingService processingService, String ticker) {
        this.ticker = ticker;
        this.processingService = processingService;
    }

    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        webSocketSession.sendMessage(new TextMessage(TRADE_SUBSCRIBE_COMMAND.replace("{ticker}", ticker)));
    }

    public void handleMessage(WebSocketSession webSocketSession, String message) throws Exception {
        if (message.contains("\""+ticker+"\"")) {
            this.processingService.process(message, null, ticker);
        }
    }
}
