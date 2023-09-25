package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GamesRequests;
import com.nus.project.capstone.model.entity.game.GamesResponse;
import com.nus.project.capstone.model.entity.game.TechCompRequests;
import com.nus.project.capstone.model.entity.game.TechCompResponse;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import com.nus.project.capstone.model.persistence.game.TechCompJpaEntities;
import com.nus.project.capstone.model.persistence.game.TechCompRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nus.project.capstone.game.web.Tools.PENDING;
import static com.nus.project.capstone.game.web.Tools.UNKNOWN;

@Slf4j
@RestController
@RequestMapping("/tech-comp")
public class TechCompController {

    private final TechCompRepository techCompRepository;
    private final GameTeamRepository gameTeamRepository;


    @Autowired
    public TechCompController(TechCompRepository techCompRepository, GameTeamRepository gameTeamRepository) {
        this.techCompRepository = techCompRepository;
        this.gameTeamRepository = gameTeamRepository;
    }

    @PostMapping("/create-tech-comp")
    public ResponseEntity<GeneralMessageEntity> createTechComp(@RequestBody TechCompRequests t) {

        if (t.getGameTeamIdHost() == null || t.getGameTeamIdOppo() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Host id & Oppo id must be provided").build());
        }

        val a = gameTeamRepository.findById(UUID.fromString(t.getGameTeamIdHost()));
        if (a.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team Host not found").build());
        val b = gameTeamRepository.findById(UUID.fromString(t.getGameTeamIdOppo()));
        if (b.isEmpty()) return ResponseEntity.ok(GeneralMessageEntity.builder().data("Team Oppo not found").build());

        t.setDateTime(Instant.now());
        val techJpa = TechCompJpaEntities.toJpaEntity(t);
        techJpa.setGamesTeam(new ArrayList<>());
        techJpa.addToGamesTeam(a.get());
        techJpa.addToGamesTeam(b.get());

        techJpa.setGameStatus(PENDING);
        techJpa.setGameOutcome(PENDING);

        val techComp = techCompRepository.save(techJpa);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(techComp.getId()).build());
    }

    @PutMapping("/update-tech-comp")
    public ResponseEntity<GeneralMessageEntity> updateTechCompInterface(@RequestBody TechCompRequests updateTechCompRequests) {
        return updateTechComp(updateTechCompRequests);
    }

    private ResponseEntity<GeneralMessageEntity> updateTechComp(@RequestBody TechCompRequests updateTechCompRequests) {

        if (updateTechCompRequests.getId() == null) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("Tech Comp id must be provided").build());
        }

        if (techCompRepository.findById(updateTechCompRequests.getId()).isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("Tech Comp %s is not found", updateTechCompRequests.getId())).build());
        }

        var techComp = techCompRepository.findById(updateTechCompRequests.getId()).get();
        techComp = techComp.updateJpaEntity(updateTechCompRequests);

        val t = techCompRepository.save(techComp);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(t.getId()).build());
    }

    @PostMapping("/view-tech-comp")
    public ResponseEntity<GeneralMessageEntity> readTechComps(@RequestBody TechCompRequests t) {

        val techComp = techCompRepository.findById(t.getId());
        return techComp.map(techCompJpaEntities -> ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(TechCompResponse.toTechCompResponse(techCompJpaEntities)).build())).orElseGet(() -> ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("No tech comp found").build()));
    }

    @GetMapping("/view-all-tech-comps")
    public ResponseEntity<GeneralMessageEntity> getAllGames() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(techCompRepository.findAll().stream()
                .map(TechCompResponse::toTechCompResponse).collect(Collectors.toList())).build());
    }
}
