package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.entity.game.GameTeamRequests;
import com.nus.project.capstone.model.entity.game.GameTeamResponse;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.base.UserRepository;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/games")
public class GameTeamController {

    private final GameTeamRepository gameTeamRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameTeamController(GameTeamRepository gameTeamRepository, UserRepository userRepository) {
        this.gameTeamRepository = gameTeamRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create-team")
    public ResponseEntity<GeneralMessageEntity> createTeam(@RequestBody GameTeamRequests g) {
        // force set default again
        g.setQualificationRoundNumMatchesPlayed(0);
        g.setIsQualifiedForElimination(false);

        var team = gameTeamRepository.save(GameTeamJpaEntities.toJpaEntity(g));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(team.getId()).build());
    }

    @GetMapping("/view-team")
    public ResponseEntity<GeneralMessageEntity> readTeam(@RequestBody GameTeamRequests g) {

        val o = gameTeamRepository.findById(g.getId());
        val i = o.map(gameTeamJpa -> {
            val gameTeamResponse = GameTeamResponse.toGameTeamResponse(gameTeamJpa);
            gameTeamResponse.setUserResponses(gameTeamJpa.getUsers() == null ? null :
                    gameTeamJpa.getUsers().stream().map(UserResponse::toUserResponse).collect(Collectors.toList()));
            return gameTeamResponse;
        }).orElse(null);

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(i).build());
    }

    @PutMapping("/update-team")
    public ResponseEntity<GeneralMessageEntity> updateTeamInterface(@RequestBody GameTeamRequests updateGameTeamRequests){
        return updateTeam(updateGameTeamRequests);
    }

    private ResponseEntity<GeneralMessageEntity> updateTeam(@RequestBody GameTeamRequests updateGameTeamRequests) {

        if (updateGameTeamRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Game team id must be provided").build());
        }

        if (gameTeamRepository.findById(updateGameTeamRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("Game Team %s is not found", updateGameTeamRequests.getId())).build());
        }

        var team = gameTeamRepository.findById(updateGameTeamRequests.getId()).get();
        team = team.updateJpaEntity(updateGameTeamRequests);

        if (updateGameTeamRequests.getUserIds() != null) {
            List<UserJpaEntities> originalUsersInTeam = userRepository.findAllByGameTeamId(team.getId());
            originalUsersInTeam.forEach(u -> {
                u.setIdcTeam(null);
                userRepository.save(u);
            });

            val users = userRepository.findAllById(updateGameTeamRequests.getUserIds());
            team.setUsers(new ArrayList<>());
            users.forEach(team::addToUsers);
        }

        val t = gameTeamRepository.save(team);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(t.getId()).build());
    }

    @GetMapping("/view-all-teams")
    public ResponseEntity<GeneralMessageEntity> getAllUsers() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gameTeamRepository.findAll().stream()
                .map(GameTeamResponse::toGameTeamResponse).collect(Collectors.toList())).build());
    }
}
