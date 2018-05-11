package com.alex.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
@PropertySources({
        @PropertySource("classpath:generator.properties")
})
@EnableConfigurationProperties({PeriodsProperties.class})
@EnableScheduling
public class CandleGeneratorConfiguration {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor accountsTaskExecutor = new ThreadPoolTaskExecutor();
        accountsTaskExecutor.setCorePoolSize(1);
        accountsTaskExecutor.setMaxPoolSize(1);
        accountsTaskExecutor.setQueueCapacity(1000);
        accountsTaskExecutor.initialize();
        return accountsTaskExecutor;
    }
}