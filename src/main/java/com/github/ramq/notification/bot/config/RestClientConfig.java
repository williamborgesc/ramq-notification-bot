package com.github.ramq.notification.bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterServiceProvider;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "twitter.client")
    public TwitterClientProperties twitterClientProperties(){
        return new TwitterClientProperties();
    }

    @Bean
    public Twitter twitterApi() {
        return new TwitterServiceProvider(twitterClientProperties().getConsumerKey(), twitterClientProperties().getConsumerSecret())
                .getApi(twitterClientProperties().getAccessToken(), twitterClientProperties().getTokenSecret());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}