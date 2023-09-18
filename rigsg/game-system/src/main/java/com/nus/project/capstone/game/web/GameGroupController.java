package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GameGroupRequests;
import com.nus.project.capstone.model.entity.game.GameGroupResponse;
import com.nus.project.capstone.model.persistence.game.GameGroupJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameGroupRepository;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/games")
public class GameGroupController {

    private final GameTeamRepository gameTeamRepository;
    private final GameGroupRepository gameGroupRepository;

    @Autowired
    public GameGroupController(GameTeamRepository gameTeamRepository, GameGroupRepository gameGroupRepository) {
        this.gameTeamRepository = gameTeamRepository;
        this.gameGroupRepository = gameGroupRepository;
    }

    @PostMapping("/create-group")
    public ResponseEntity<GeneralMessageEntity> createGroup(@RequestBody GameGroupRequests gameGroupRequests) {
        var group = gameGroupRepository.save(GameGroupJpaEntities.toJpaEntity(gameGroupRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(group.getId()).build());
    }

    @PutMapping("/assign-team")
    public ResponseEntity<GeneralMessageEntity> assignTeam(@RequestBody GameGroupRequests gameGroupRequests) {
        var group = gameGroupRepository.findById(gameGroupRequests.getId());
        if (group.isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("Game Group %s is not found", gameGroupRequests.getId())).build());
        }

        var groupEntity = group.get();
        var gameTeamIds = gameGroupRequests.getGameTeamIds();
        var allGameTeamIds = gameTeamRepository.findAll();

        allGameTeamIds.forEach(t -> {
            if (gameTeamIds.contains(t.getId())){
                groupEntity.addToGameTeams(t);
            } else {
                groupEntity.removeGameTeams(t);
            }
            gameTeamRepository.save(t);
        });
        gameGroupRepository.save(groupEntity);

        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Updated Group %s", gameGroupRequests.getId())).build());
    }

    @GetMapping("/view-all-groups")
    public ResponseEntity<GeneralMessageEntity> getAllGroups() {
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gameGroupRepository.findAll().stream()
                .map(GameGroupResponse::toGameGroupResponse).collect(Collectors.toList())).build());
    }

}
