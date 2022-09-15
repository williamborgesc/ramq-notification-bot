package com.github.ramq.notification.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class RamqNotificationBot {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(RamqNotificationBot.class);
        builder.headless(false);
        builder.run(args);
    }
}
