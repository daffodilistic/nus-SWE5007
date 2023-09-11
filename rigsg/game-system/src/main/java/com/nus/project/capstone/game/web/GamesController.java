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

    @PostMapping("/game")
    public ResponseEntity<GeneralMessageEntity> createGames(@RequestBody GamesRequests g) {

        if (g.getGameTeamIdHost() == null || g.getGameTeamIdOppo() == null) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("A id & B id must be provided").build());
        }

        val a = gameTeamRepository.findById(UUID.fromString(g.getGameTeamIdHost()));
        if (a.isEmpty()) return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("Team A not found").build());
        val b = gameTeamRepository.findById(UUID.fromString(g.getGameTeamIdOppo()));
        if (b.isEmpty()) return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("Team A not found").build());

        val gamesJpa = GamesJpaEntities.toJpaEntity(g);
        gamesJpa.setGamesTeam(new ArrayList<>());
        gamesJpa.addToGamesTeam(a.get());
        gamesJpa.addToGamesTeam(b.get());

        val game = gamesRepository.save(gamesJpa);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(game.getId()).build());
    }

    @PutMapping("/game")
    public ResponseEntity<GeneralMessageEntity> updateGame(@RequestBody GamesRequests updateGamesRequests) {

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

    @GetMapping("/game")
    public ResponseEntity<GeneralMessageEntity> readGames(@RequestBody GamesRequests g) {

        val game = gamesRepository.findById(g.getId());
        return game.map(gamesJpaEntities -> ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(GamesResponse.toGamesResponse(gamesJpaEntities)).build())).orElseGet(() -> ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("No game found").build()));
    }

    @GetMapping("/games")
    public ResponseEntity<GeneralMessageEntity> getAllGames() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gamesRepository.findAll().stream()
                .map(GamesResponse::toGamesResponse).collect(Collectors.toList())).build());
    }
}
