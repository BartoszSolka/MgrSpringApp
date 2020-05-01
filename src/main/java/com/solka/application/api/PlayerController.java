package com.solka.application.api;

import com.solka.application.persistence.PlayerEntity;
import com.solka.application.persistence.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerRepository playerRepository;

    @GetMapping("/players")
    public Page<PlayerEntity> readAllPlayers(@PageableDefault Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @GetMapping("/players/{id}")
    public PlayerEntity readPlayer(@PathVariable("id") Long id) {
        return playerRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
