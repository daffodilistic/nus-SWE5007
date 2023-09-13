package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GamesRequests;
import com.nus.project.capstone.model.entity.game.GamesResponse;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import com.nus.project.capstone.model.persistence.game.GamesJpaEntities;
import com.nus.project.capstone.model.persistence.game.GamesRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nus.project.capstone.game.web.Tools.*;

@Slf4j
@RestController
@RequestMapping("/games")
public class GamesController {

    private final GameTeamRepository gameTeamRepository;

    private final GamesRepository gamesRepository;

    @Autowired
    public GamesController(GameTeamRepository gameTeamRepository, GamesRepository gamesRepository) {
        this.gameTeamRepository = gameTeamRepository;
        this.gamesRepository = gamesRepository;
    }

    @PostMapping("/create-game")
    public ResponseEntity<GeneralMessageEntity> createGames(@RequestBody GamesRequests g) {

        if (g.getGameTeamIdHost() == null || g.getGameTeamIdOppo() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Host id & Oppo id must be provided").build());
        }

        val a = gameTeamRepository.findById(UUID.fromString(g.getGameTeamIdHost()));
        if (a.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team Host not found").build());
        val b = gameTeamRepository.findById(UUID.fromString(g.getGameTeamIdOppo()));
        if (b.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team Oppo not found").build());

        val gamesJpa = GamesJpaEntities.toJpaEntity(g);
        gamesJpa.setGamesTeam(new ArrayList<>());
        gamesJpa.addToGamesTeam(a.get());
        gamesJpa.addToGamesTeam(b.get());

        gamesJpa.setGameScoreHost(0);
        gamesJpa.setGameScoreOppo(0);
        gamesJpa.setGameStatus(PENDING);
        gamesJpa.setGameOutcome(UNKNOWN);

        val game = gamesRepository.save(gamesJpa);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(game.getId()).build());
    }

    @PutMapping("/update-game")
    public ResponseEntity<GeneralMessageEntity> updateGameInterface(@RequestBody GamesRequests updateGamesRequests) {
        return updateGame(updateGamesRequests);
    }

    @PutMapping("/update-ongoing-status")
    public ResponseEntity<GeneralMessageEntity> updateGameStatus(@RequestBody GamesRequests updateGamesRequests) {
        if (updateGamesRequests.getGameTeamIdHost() == null &&
            updateGamesRequests.getGameTeamIdOppo() == null &&
            updateGamesRequests.getGameScoreHost() == null &&
            updateGamesRequests.getGameScoreOppo() == null &&
            updateGamesRequests.getGameStatus() == null &&
            updateGamesRequests.getGameOutcome() == null) {
            updateGamesRequests.setGameStatus(ONGOING);
            return updateGame(updateGamesRequests);
        } else {
            return genericFailureMessage();
        }
    }

    @PutMapping("/update-scores")
    public ResponseEntity<GeneralMessageEntity> updateGameScores(@RequestBody GamesRequests updateGamesRequests) {
        if (updateGamesRequests.getGameOutcome() == null) {

            val a = gameTeamRepository.findById(UUID.fromString(updateGamesRequests.getGameTeamIdHost()));
            if (a.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team Host not found").build());
            val b = gameTeamRepository.findById(UUID.fromString(updateGamesRequests.getGameTeamIdOppo()));
            if (b.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team Oppo not found").build());

            var teamHost = a.get();
            var teamOppo = b.get();

            String outcome = calculateOutcome(updateGamesRequests.getGameScoreHost(), updateGamesRequests.getGameScoreOppo());
            updateGamesRequests.setGameStatus(DONE);
            updateGamesRequests.setGameOutcome(outcome);
            updateGame(updateGamesRequests);

            teamHost.setQualificationRoundScore(teamHost.getQualificationRoundScore() + updateGamesRequests.getGameScoreHost());
            teamOppo.setQualificationRoundScore(teamOppo.getQualificationRoundScore() + updateGamesRequests.getGameScoreOppo());

            teamHost.setQualificationRoundPoint(teamHost.getQualificationRoundPoint() + rulesMap.get(outcome));
            teamOppo.setQualificationRoundPoint(teamOppo.getQualificationRoundPoint() + rulesMap.get(flipOutcome(outcome)));

            teamHost.setQualificationRoundNumMatchesPlayed(teamHost.getQualificationRoundNumMatchesPlayed() + 1);
            teamOppo.setQualificationRoundNumMatchesPlayed(teamOppo.getQualificationRoundNumMatchesPlayed() + 1);

            gameTeamRepository.save(teamHost);
            gameTeamRepository.save(teamOppo);

            return ResponseEntity.ok(GeneralMessageEntity.builder().data(String.format("Scores are updated for team host %s and team oppo %s", teamHost.getId(), teamOppo.getId())).build());
        } else {
            return genericFailureMessage();
        }
    }

    private ResponseEntity<GeneralMessageEntity> updateGame(@RequestBody GamesRequests updateGamesRequests) {

        if (updateGamesRequests.getId() == null) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("Game id must be provided").build());
        }

        if (gamesRepository.findById(updateGamesRequests.getId()).isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("Game %s is not found", updateGamesRequests.getId())).build());
        }

        var game = gamesRepository.findById(updateGamesRequests.getId()).get();
        game = game.updateJpaEntity(updateGamesRequests);

        val g = gamesRepository.save(game);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(g.getId()).build());
    }

    @PostMapping("/view-game")
    public ResponseEntity<GeneralMessageEntity> readGames(@RequestBody GamesRequests g) {

        val game = gamesRepository.findById(g.getId());
        return game.map(gamesJpaEntities -> ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(GamesResponse.toGamesResponse(gamesJpaEntities)).build())).orElseGet(() -> ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("No game found").build()));
    }

    @GetMapping("/view-all-games")
    public ResponseEntity<GeneralMessageEntity> getAllGames() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gamesRepository.findAll().stream()
                .map(GamesResponse::toGamesResponse).collect(Collectors.toList())).build());
    }
}
