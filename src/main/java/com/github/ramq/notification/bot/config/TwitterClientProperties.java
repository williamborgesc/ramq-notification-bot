package com.github.ramq.notification.bot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterClientProperties {

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String tokenSecret;
}
