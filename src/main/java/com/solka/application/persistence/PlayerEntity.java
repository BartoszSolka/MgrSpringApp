package com.solka.application.persistence;

import com.solka.application.domain.Position;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private TeamEntity teamEntity;

    private String name;

    private String hashtag;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Position position;
}
