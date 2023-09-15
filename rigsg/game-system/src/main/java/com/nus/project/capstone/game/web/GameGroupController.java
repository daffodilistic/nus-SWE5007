package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GameGroupResponse;
import com.nus.project.capstone.model.persistence.game.GameGroupRepository;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/view-all-groups")
    public ResponseEntity<GeneralMessageEntity> getAllGroups() {
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gameGroupRepository.findAll().stream()
                .map(GameGroupResponse::toGameGroupResponse).collect(Collectors.toList())).build());
    }

}
