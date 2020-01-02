package com.solka.application;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.solka.application.csv.CsvPlayer;
import com.solka.application.domain.Position;
import com.solka.application.persistence.PlayerEntity;
import com.solka.application.persistence.PlayerRepository;
import com.solka.application.persistence.TeamEntity;
import com.solka.application.persistence.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Bootstrap implements ApplicationRunner {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        if (!playerRepository.findAll().isEmpty()) {
            return;
        }

        parseCsv();

        final TeamEntity team1 = TeamEntity.builder()
                .name("FC Barcelona")
                .city("Barcelona")
                .coach("Valverde")
                .build();

        final TeamEntity team2 = TeamEntity.builder()
                .name("Real Madrid")
                .city("Madrid")
                .coach("Zidane")
                .build();

        final TeamEntity team3 = TeamEntity.builder()
                .name("FC Bayern")
                .city("Monachium")
                .coach("CoachFCB")
                .build();

        final TeamEntity team4 = TeamEntity.builder()
                .name("Juventus")
                .city("Torino")
                .coach("Sarri")
                .build();

        TeamEntity savedTeam1 = teamRepository.save(team1);
        TeamEntity savedTeam2 = teamRepository.save(team2);
        TeamEntity savedTeam3 = teamRepository.save(team3);
        TeamEntity savedTeam4 = teamRepository.save(team4);

        PlayerEntity player1 = PlayerEntity.builder()
                .name("Leo Messi")
                .hashtag("Messi")
                .position(Position.ATTACKER)
                .teamEntity(savedTeam1)
                .birthDate(LocalDate.now().minusYears(30))
                .build();

        PlayerEntity player2 = PlayerEntity.builder()
                .name("Luka Modric")
                .hashtag("Modric")
                .position(Position.MIDFIELDER)
                .teamEntity(savedTeam2)
                .birthDate(LocalDate.now().minusYears(29))
                .build();

        PlayerEntity player3 = PlayerEntity.builder()
                .name("Robert Lewandowski")
                .hashtag("RL9")
                .position(Position.ATTACKER)
                .teamEntity(savedTeam3)
                .birthDate(LocalDate.now().minusYears(29))
                .build();

        PlayerEntity player4 = PlayerEntity.builder()
                .name("Cristiano Ronaldo")
                .hashtag("CR7")
                .position(Position.ATTACKER)
                .teamEntity(savedTeam4)
                .birthDate(LocalDate.now().minusYears(29))
                .build();

        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);
        playerRepository.save(player4);
    }

    private void parseCsv() throws IOException {
        try (Reader reader = Files.newBufferedReader(
                Paths.get("src/main/resources/england-premier-league-players-2018-to-2019-stats.csv")))
        {
            CsvToBean<CsvPlayer> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CsvPlayer.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<CsvPlayer> csvPlayerIterator = csvToBean.iterator();

            while (csvPlayerIterator.hasNext()) {

                CsvPlayer csvPlayer = csvPlayerIterator.next();

                Optional<TeamEntity> teamOptional = teamRepository.findByName(csvPlayer.getClub());
                TeamEntity teamEntity;
                if (!teamOptional.isPresent()) {
                    teamEntity = TeamEntity.builder()
                            .name(csvPlayer.getClub())
                            .build();

                    teamRepository.save(teamEntity);
                } else {
                    teamEntity = teamOptional.get();
                }

                String[] splitName = csvPlayer.getFullName().split(" ");
                String hashtag = splitName[splitName.length - 1];

                PlayerEntity playerEntity = PlayerEntity.builder()
                        .position(getPosition(csvPlayer.getPosition()))
                        .name(csvPlayer.getFullName())
                        .hashtag(hashtag)
                        .teamEntity(teamEntity)
                        .build();

                playerRepository.save(playerEntity);
            }
        }
    }

    private Position getPosition(String position) {
        return Position.ATTACKER;
    }
}
