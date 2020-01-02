package com.solka.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<TweetEntity, Long> {
}
