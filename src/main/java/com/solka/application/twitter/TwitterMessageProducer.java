package com.solka.application.twitter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.CollectionUtils;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;

import java.util.List;

@Slf4j
@Getter
@Setter
public class TwitterMessageProducer extends MessageProducerSupport {
    private final TwitterStream twitterStream;
    private List<Long> follows;
    private List<String> terms;
    private StatusListener statusListener;
    private FilterQuery filterQuery;

    public TwitterMessageProducer(TwitterStream twitterStream, MessageChannel outputChannel) {
        this.twitterStream = twitterStream;
        setOutputChannel(outputChannel);
    }
    @Override
    protected void onInit() {
        super.onInit();
        statusListener = new StatusListener();
        long[] followsArray = null;
        if (!CollectionUtils.isEmpty(follows)) {
            followsArray = new long[follows.size()];
            for (int i = 0; i < follows.size(); i++) {
                followsArray[i] = follows.get(i);
            }
        }
        String[] termsArray = null;
        if (!CollectionUtils.isEmpty(terms)) {
            termsArray = terms.toArray(new String[0]);
        }
        filterQuery = new FilterQuery(0, followsArray, termsArray);
    }
    @Override
    public void doStart() {
        twitterStream.addListener(statusListener);
        twitterStream.filter(filterQuery);
    }
    @Override
    public void doStop() {
        twitterStream.cleanUp();
        twitterStream.clearListeners();
    }
    class StatusListener extends StatusAdapter {
        @Override
        public void onStatus(Status status) {
            sendMessage(MessageBuilder.withPayload(status).build());
        }
        @Override
        public void onException(Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        @Override
        public void onStallWarning(StallWarning warning) {
            log.warn(warning.toString());
        }
    }
}
