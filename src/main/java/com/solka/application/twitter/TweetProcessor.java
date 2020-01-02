package com.solka.application.twitter;

import com.solka.application.persistence.PlayerEntity;
import com.solka.application.persistence.PlayerRepository;
import com.solka.application.persistence.TweetEntity;
import com.solka.application.persistence.TweetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TweetProcessor {

    private final TweetRepository tweetRepository;
    private final PlayerRepository playerRepository;
    private final Map<String, PlayerEntity> playersMap = new HashMap<>();
    private int count = 0;

    @Transactional
    public void process(Message<?> message) {
        if (playersMap.isEmpty()) {
            initializePlayersMap();
        }
        Status status = (Status) message.getPayload();
        log.info("Count: {}, Processing: {}", count++, status.getId());
        Arrays.asList(status.getHashtagEntities())
                .forEach(hashtag -> {
                    String text = hashtag.getText();
                    if (playersMap.containsKey(text)) {
                        TweetEntity tweet = TweetEntity.builder()
                                .playerEntity(playersMap.get(text))
                                .content(status.getText())
                                .build();
                        tweetRepository.save(tweet);
                    }
                });
        log.info("Processed: {}", status.getId());
    }

    private void initializePlayersMap() {
        playerRepository.findAllStream()
                .forEach(player -> playersMap.put(player.getHashtag(), player));
    }
}
