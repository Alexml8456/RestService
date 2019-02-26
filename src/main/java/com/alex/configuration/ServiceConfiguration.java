package com.alex.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class ServiceConfiguration {
    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor accountsTaskExecutor = new ThreadPoolTaskExecutor();
        accountsTaskExecutor.setCorePoolSize(5);
        accountsTaskExecutor.setMaxPoolSize(10);
        accountsTaskExecutor.setQueueCapacity(1000);
        accountsTaskExecutor.initialize();
        return accountsTaskExecutor;
    }
}
