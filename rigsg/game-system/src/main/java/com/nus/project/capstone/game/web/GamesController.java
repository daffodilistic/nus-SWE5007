package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.entity.game.GameTeamRequests;
import com.nus.project.capstone.model.entity.game.GameTeamResponse;
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
    public ResponseEntity<GeneralMessageEntity> createGame(@RequestBody GamesRequests g) {

        if (g.getGameTeamIdA() == null || g.getGameTeamIdB() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("A id & B id must be provided").build());
        }

        val a = gameTeamRepository.findById(UUID.fromString(g.getGameTeamIdA()));
        if (a.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team A not found").build());
        val b = gameTeamRepository.findById(UUID.fromString(g.getGameTeamIdB()));
        if (b.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team A not found").build());

        val gamesJpa = GamesJpaEntities.toJpaEntity(g);
        gamesJpa.setGamesTeam(new ArrayList<>());
        gamesJpa.addToGamesTeam(a.get());
        gamesJpa.addToGamesTeam(b.get());

        val game = gamesRepository.save(gamesJpa);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(game.getId()).build());
    }

    @GetMapping("/game")
    public ResponseEntity<GeneralMessageEntity> readGame(@RequestBody GamesRequests g) {

        val game = gamesRepository.findById(g.getId());
        return game.map(gamesJpaEntities -> ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(GamesResponse.toGamesResponse(gamesJpaEntities)).build())).orElseGet(() -> ResponseEntity.ok(GeneralMessageEntity.builder().data("No game found").build()));
    }

    @GetMapping("/game")
    public ResponseEntity<GeneralMessageEntity> getAllGames() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gamesRepository.findAll().stream()
                .map(GamesResponse::toGamesResponse).collect(Collectors.toList())).build());
    }
}
