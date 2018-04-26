package com.alex.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BitmexCommands {
    public static final String TRADE_SUBSCRIBE_COMMAND = "{\"op\": \"subscribe\", \"args\": [\"trade:{ticker}\"]}";;
}
