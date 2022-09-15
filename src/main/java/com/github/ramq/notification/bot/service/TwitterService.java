package com.github.ramq.notification.bot.service;

import com.github.ramq.notification.bot.dto.twitter.Tweet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterService {

    private final Twitter twitterApi;

    @Value("${twitter.resource.tweets}")
    private String tweetsUrl;

    public void createTweet(String tweetMessage) {
        log.info("Creating tweet with message={}", tweetMessage);
        Tweet notification = new Tweet(tweetMessage);
        String result = twitterApi.restOperations().postForObject(tweetsUrl, notification, String.class);
        log.debug("Response from tweeter api={}", result);
        log.info("End createTweet");
    }
}
