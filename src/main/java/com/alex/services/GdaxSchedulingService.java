package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
public class GdaxSchedulingService {

    @Autowired
    private GdaxConnectionService connectionService;
    @Autowired
    private GdaxSessionStorage sessionStorage;
    @Autowired
    private GdaxProcessingService processingService;

    @Scheduled(fixedDelay = 60000)
    public void verifyStalePrice() {
        try {
            LocalDateTime fiveMinsBefore = LocalDateTime.now(ZoneId.of("GMT-0")).truncatedTo(ChronoUnit.MINUTES).minusMinutes(2);
            if (processingService.getLastTradeTime() != null &&
                    processingService.getLastTradeTime().isBefore(fiveMinsBefore)) {
                log.error("Stale price is found. Reconnect will be initiated");
                scheduledReconnect();
            }
        } catch (Exception e) {
            log.error("Verify Stale Price failed", e);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void reconnect() throws InterruptedException, IOException, DeploymentException {
        if (!isConnected()) {
            Optional<WebSocketConnectionManager> connectionManager = ofNullable(connectionService.getConnectionManager());
            if (connectionManager.isPresent()) {
                log.warn("Reconnecting");
                try {
                    if (sessionStorage.getSession() != null && sessionStorage.getSession().isOpen()) {
                        sessionStorage.getSession().close();
                    }
                    connectionManager.get().stop();
                    SECONDS.sleep(1);
                    connect();
                } catch (Exception e) {
                    log.error("Can't reconnect. " + e.getMessage(), e);
                }
            } else {
                connect();
            }
        }
    }

    //    @Scheduled(cron = "0 0 */6 * * *")
    public void scheduledReconnect() {
        Optional<WebSocketConnectionManager> connectionManager = ofNullable(connectionService.getConnectionManager());
        connectionManager.ifPresent(cm -> {
            log.warn("Terminating connection on {}", LocalDateTime.now());
            try {
                sessionStorage.getSession().close();
                connectionManager.get().stop();
            } catch (Exception e) {
                log.error("Can't reconnect. " + e.getMessage(), e);
            }
        });
    }

    private void connect() throws IOException, DeploymentException, InterruptedException {
        connectionService.connect();
        SECONDS.sleep(1);

        if (isConnected()) {
            log.info("Connected");
        } else {
            log.error("Can't reconnect");
        }
    }

    private boolean isConnected() {
        WebSocketSession session = sessionStorage.getSession();
        return session != null && session.isOpen();
    }

}