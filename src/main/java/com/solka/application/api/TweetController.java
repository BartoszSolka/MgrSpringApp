package com.solka.application.api;

import com.solka.application.persistence.TweetEntity;
import com.solka.application.persistence.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TweetController {

    private final TweetRepository repository;

    @GetMapping("/players/{playerId}/tweets")
    public List<TweetEntity> getTweetsByPlayer(@PathVariable("playerId") Long playerId) {
        return repository.findAllByPlayerEntity_Id(playerId);
    }
}
