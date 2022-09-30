package com.github.ramq.notification.bot.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final TwitterService twitterService;

    @Value("${application.notification.tweet-message}")
    private String tweetMessage;

    public void sendNotification() {
        try {
            twitterService.createTweet(tweetMessage);
        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong when trying to notify:", ex);
        }
    }
}