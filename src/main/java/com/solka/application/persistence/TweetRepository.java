package com.solka.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<TweetEntity, Long> {

    List<TweetEntity> findAllByPlayerEntity_Id(Long playerId);
}
