package com.solka.application;

import com.solka.application.domain.PlayerTagsMapper;
import com.solka.application.twitter.TweetProcessor;
import com.solka.application.twitter.TwitterMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TwitterConfig {

    private final TweetProcessor tweetProcessor;
    private final PlayerTagsMapper playerTagsMapper;

    @Bean
    TwitterStreamFactory twitterStreamFactory() {
        return new TwitterStreamFactory();
    }

    @Bean
    TwitterStream twitterStream(TwitterStreamFactory twitterStreamFactory) {
        return twitterStreamFactory.getInstance();
    }

    @Bean
    MessageChannel outputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    TwitterMessageProducer twitterMessageProducer(
            TwitterStream twitterStream, MessageChannel outputChannel) {
        TwitterMessageProducer twitterMessageProducer =
                new TwitterMessageProducer(twitterStream, outputChannel);

        twitterMessageProducer.setTerms(playerTagsMapper.getTags());
        return twitterMessageProducer;
    }

    @Bean
    IntegrationFlow twitterFlow(MessageChannel outputChannel) {
        return IntegrationFlows.from(outputChannel)
                .handle(tweetProcessor::process)
                .get();
    }
}
