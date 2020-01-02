package com.solka.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    @Query("select p from PlayerEntity p")
    Stream<PlayerEntity> findAllStream();
}
