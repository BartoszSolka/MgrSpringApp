package com.solka.application.domain;

import com.solka.application.persistence.PlayerEntity;
import com.solka.application.persistence.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlayerTagsMapper {

    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public List<String> getTags() {
        return playerRepository.findAllStream()
                .map(PlayerEntity::getHashtag)
                .map("#"::concat)
                .collect(Collectors.toList());
    }
}
