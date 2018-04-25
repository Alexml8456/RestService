package com.alex.services;

import com.alex.interfaces.BasicProcessingService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class GdaxProcessingService implements BasicProcessingService {

    @Getter
    private LocalDateTime lastTradeTime;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    @Override
    public void process(String message, String filter, String ticker) {
        if (isSuitableMessage(message)) {
            JSONObject object = new JSONObject(message);
            ticker = object.getString("product_id");
            String timeStr = object.getString("time");
            LocalDateTime timestamp = LocalDateTime.parse(timeStr.substring(0, timeStr.lastIndexOf(".")), formatter);
            BigDecimal rate = BigDecimal.valueOf(object.getDouble("price"));
            BigDecimal volume = BigDecimal.valueOf(object.getDouble("last_size"));
            String direction = object.getString("side");
            BigDecimal baseVolume = volume.multiply(rate);
            log.info("instrument = {}; direction = {}; volume = {}; rate = {}; timestamp = {}", ticker, direction, baseVolume, rate, timestamp);
            lastTradeTime = LocalDateTime.now(ZoneId.of("GMT-0"));
        }
    }

    private boolean isSuitableMessage(String message) {
        return message.contains("trade_id");
    }
}