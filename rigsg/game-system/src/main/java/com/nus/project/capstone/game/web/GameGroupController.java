package com.nus.project.capstone.game.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.game.GameGroupRequests;
import com.nus.project.capstone.model.entity.game.GameGroupResponse;
import com.nus.project.capstone.model.persistence.game.GameGroupJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameGroupRepository;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.nus.project.capstone.game.web.Tools.*;

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
        var allGameTeamIds = gameTeamRepository.findAllById(gameTeamIds);

        groupEntity.getGameTeams().forEach(t -> {
            t.setGameGroup(null);
            gameTeamRepository.save(t);
        });
        groupEntity.setGameTeams(new ArrayList<>());

        if (!gameTeamIds.isEmpty()){
            allGameTeamIds.forEach(t -> {
                groupEntity.addToGameTeams(t);
                gameTeamRepository.save(t);
            });
        }

        gameGroupRepository.save(groupEntity);

        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Updated Group %s", gameGroupRequests.getId())).build());
    }

    @GetMapping("/view-all-groups")
    public ResponseEntity<GeneralMessageEntity> getAllGroups() {
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(gameGroupRepository.findAll().stream()
                .map(GameGroupResponse::toGameGroupResponse).collect(Collectors.toList())).build());
    }

    @PostMapping("/check-qualification-status")
    public ResponseEntity<GeneralMessageEntity> checkQualify(@RequestBody GameGroupRequests gameGroupRequests) {
        var allGameTeams = gameTeamRepository.findAll()
                .stream()
                .filter(t -> t.getGameGroup().getId().equals(gameGroupRequests.getId()))
                .toList();

        if (allGameTeams.size() != DEFAULT_TEAM_SIZE){
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("The group does not have correct team size: %s", DEFAULT_TEAM_SIZE)).build());
        }

        if (allGameTeams.stream()
                .filter(t -> t.getQualificationRoundNumMatchesPlayed() >= (DEFAULT_TEAM_SIZE - 1))
                .toList()
                .size() != DEFAULT_TEAM_SIZE){
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("Not all teams have played all matches!")).build());
        }

        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Group %s is ready for qualification", gameGroupRequests.getId())).build());
    }

    @PostMapping("/qualify-group")
    public ResponseEntity<GeneralMessageEntity> qualifyGroup(@RequestBody GameGroupRequests gameGroupRequests) {
        var group = gameGroupRepository.findById(gameGroupRequests.getId());
        if (group.isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("Game Group %s is not found", gameGroupRequests.getId())).build());
        }

        if (checkQualify(gameGroupRequests).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data("Qualification pre-condition not met! Please use qualify-group api to verify the status first").build());
        }

        var allGameTeams = gameTeamRepository.findAll()
                .stream()
                .filter(t -> t.getGameGroup().getId().equals(gameGroupRequests.getId()))
                .toList();
        var allGameTeamsCopy = new ArrayList<GameTeamJpaEntities>(allGameTeams);
        var topNTeams = getTopNTeams(DEFAULT_TOP, allGameTeamsCopy);

        if (topNTeams.size() == DEFAULT_TOP){
            var id1 = topNTeams.get(0).getId();
            var id2 = topNTeams.get(1).getId();
            if (gameTeamRepository.findById(id1).isEmpty() || gameTeamRepository.findById(id2).isEmpty()){
                return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                        .data("Team ID not found!").build());
            }

            var groupEntity = group.get();

            groupEntity.setGameTeamIdFirst(id1);
            groupEntity.setGameTeamIdSecond(id2);
            gameGroupRepository.save(groupEntity);

            var team1 = gameTeamRepository.findById(id1).get();
            var team2 = gameTeamRepository.findById(id2).get();
            team1.setIsQualifiedForElimination(true);
            team2.setIsQualifiedForElimination(true);
            gameTeamRepository.save(team1);
            gameTeamRepository.save(team2);

            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("Updated Group: %s", gameGroupRequests.getId())).build());
        } else {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("A tie found! Need a re-match! All teams point & score is: [%s]",
                            topNTeams.stream()
                                    .map(t->t.toString())
                                    .collect(Collectors.joining(";")))).build());
        }
    }

}
