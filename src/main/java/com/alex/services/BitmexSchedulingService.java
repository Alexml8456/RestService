package com.alex.services;

import com.alex.model.Candle;
import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
public class BitmexSchedulingService {

    @Autowired
    private BitmexConnectionService connectionService;
    @Autowired
    private BitmexSessionStorage sessionStorage;
    @Autowired
    private BitmexProcessingService processingService;
    @Autowired
    private AnalyzeProvider analyzeProvider;

    @Autowired
    private CandleGenerationService candleGenerationService;


    @Scheduled(fixedDelay = 60000)
    public void verifyStalePrice() {
        try {
            LocalDateTime fiveMinsBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(2);
            if (processingService.getLastTradeTime() != null &&
                    processingService.getLastTradeTime().isBefore(fiveMinsBefore)) {
                log.error("Stale price is found. Reconnect will be initiated");
                scheduledReconnect();
            }
        } catch (Exception e) {
            log.error("Verify Stale Price failed", e);
        }
    }


    //@Scheduled(fixedDelay = 1000)
    @Scheduled(fixedDelay = 1000000000)
    public void reconnect() throws InterruptedException, IOException, DeploymentException {
        if (!isConnected()) {
            Optional<WebSocketConnectionManager> connectionManager = ofNullable(connectionService.getConnectionManager());
            if (connectionManager.isPresent()) {
                log.warn("Reconnecting");
                try {
                    if (sessionStorage.getSession() != null && sessionStorage.getSession().isOpen()) {
                        log.info("Closing session");
                        sessionStorage.getSession().close();
                    }
                    connectionManager.get().stop();
                    SECONDS.sleep(2);
                    connect();
                } catch (Exception e) {
                    log.error("Can't reconnect. " + e.getMessage(), e);
                }
            } else {
                connect();
            }
        }
    }

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
        SECONDS.sleep(2);
        if (isConnected()) {
            log.info("Connected");
        } else {
            log.error("Can't connect");
        }
    }

    private boolean isConnected() {
        WebSocketSession session = sessionStorage.getSession();
        return session != null && (session.isOpen() || sessionStorage.isConnecting());
    }

//    @Scheduled(cron = "* 0/5 * ? * *")
//    public void cleanHistory() {
//        LocalDateTime fiveMinutesBeforeNow = DateTime.getGMTTimeToMinutes().minusMinutes(5);
//        for (Iterator<Map.Entry<LocalDateTime, JSONArray>> it = processingService.getTradesHistory().entrySet().iterator(); it.hasNext(); ) {
//            Map.Entry<LocalDateTime, JSONArray> entry = it.next();
//            if (entry.getKey().isBefore(fiveMinutesBeforeNow)) {
//                it.remove();
//                log.info("Old trades removed!");
//            }
//        }
//    }

    @Scheduled(cron = "0 11 18 ? * *")
    public void stopSession() throws IOException {
        sessionStorage.getSession().close();
        log.info("Session closed");
    }

//    @Scheduled(cron = "5 * * ? * *")
//    public void test() {
//        processingService.getTradeData();
//    }

    @Scheduled(cron = "0 0/5 * ? * *")
    public void test() {
        candleGenerationService.getCharts().forEach((period, candle) -> {
            Map<LocalDateTime, Candle> candles = new TreeMap<>(Comparator.reverseOrder());
            candleGenerationService.getCharts().get(period).forEach((key, value) ->
                    candles.put(key, value));
//            int i=0;
//            for (Map.Entry<LocalDateTime, Candle> entry : candles.entrySet()) {
//                if (i>10){
//                    break;
//                }
//                log.info("Period = {} / Key = {} / Value = {}", period, entry.getKey(), entry.getValue());
//                i++;
//            }

            analyzeProvider.processTrading(period, candles);
        });
    }


}
