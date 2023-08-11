package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.entity.idc.IdcTeamRequests;
import com.nus.project.capstone.model.entity.idc.IdcTeamResponse;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.base.UserRepository;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcTeamRepository;
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
@RequestMapping("/idcteam")
public class IdcTeamController {

    private final IdcTeamRepository idcTeamRepository;
    private final UserRepository userRepository;

    @Autowired
    public IdcTeamController(IdcTeamRepository idcTeamRepository, UserRepository userRepository) {
        this.idcTeamRepository = idcTeamRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create-team")
    public ResponseEntity<GeneralMessageEntity> createTeam(@RequestBody IdcTeamRequests idcTeamRequests) {
        var team = idcTeamRepository.save(IdcTeamJpaEntities.toJpaEntity(idcTeamRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(team.getId()).build());
    }

    @PostMapping("/view-team")
    public ResponseEntity<GeneralMessageEntity> readTeam(@RequestBody IdcTeamRequests idcTeamRequests) {

        val o = idcTeamRepository.findById(idcTeamRequests.getId());
        val i = o.map(idcTeamJpa -> {
            val idcTeamResponse = IdcTeamResponse.toIdcTeamResponse(idcTeamJpa);
            idcTeamResponse.setUserResponses(idcTeamJpa.getUsers() == null ? null :
                    idcTeamJpa.getUsers().stream().map(UserResponse::toUserResponse).collect(Collectors.toList()));
            return idcTeamResponse;
        }).orElse(null);

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(i).build());
    }

    @PutMapping("/qualify-team")
    public ResponseEntity<GeneralMessageEntity> qualifyTeam(@RequestBody IdcTeamRequests idcTeamRequests) {
        if(idcTeamRequests.getTeamName() == null &&
           idcTeamRequests.getIdcGroupId() == null &&
           idcTeamRequests.getAgeGroup() == null &&
           idcTeamRequests.getRankFirstStage() == null &&
           idcTeamRequests.getTeacherId() == null &&
           idcTeamRequests.getUserIds() == null){
            return updateTeam(idcTeamRequests);
        } else{
            return genericFailureMessage();
        }
    }

    @PutMapping("/assign-user")
    public ResponseEntity<GeneralMessageEntity> assignUser(@RequestBody IdcTeamRequests idcTeamRequests) {
        if(idcTeamRequests.getIsQualifiedPromo() == null &&
                idcTeamRequests.getIsQualifiedFinal() == null &&
                idcTeamRequests.getIsQualifiedFinalSecondStage() == null &&
                idcTeamRequests.getIdcGroupId() == null &&
                idcTeamRequests.getAgeGroup() == null &&
                idcTeamRequests.getRankFirstStage() == null &&
                idcTeamRequests.getTeacherId() == null ){
            return updateTeam(idcTeamRequests);
        } else{
            return genericFailureMessage();
        }
    }

    private ResponseEntity<GeneralMessageEntity> updateTeam(IdcTeamRequests updateIdcTeamRequests) {

        if (updateIdcTeamRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Idc team id must be provided").build());
        }

        if (idcTeamRepository.findById(updateIdcTeamRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("IDC Team %s is not found", updateIdcTeamRequests.getId())).build());
        }

        var team = idcTeamRepository.findById(updateIdcTeamRequests.getId()).get();
        team = team.updateJpaEntity(updateIdcTeamRequests);

        if (updateIdcTeamRequests.getUserIds() != null) {
            List<UserJpaEntities> originalUsersInTeam = userRepository.findAllByIdcTeamId(team.getId());
            originalUsersInTeam.forEach(u -> {
                u.setIdcTeam(null);
                userRepository.save(u);
            });

            val users = userRepository.findAllById(updateIdcTeamRequests.getUserIds());
            team.setUsers(new ArrayList<>());
            users.forEach(team::addToUsers);
        }

        val t = idcTeamRepository.save(team);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(t.getId()).build());
    }

    @GetMapping("/view-all-teams")
    public ResponseEntity<GeneralMessageEntity> getAllUsers() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(idcTeamRepository.findAll().stream()
                .map(IdcTeamResponse::toIdcTeamResponse).collect(Collectors.toList())).build());
    }

    private ResponseEntity<GeneralMessageEntity> genericFailureMessage(){
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data("Update failed. Request contains other illegal params. Pls check.").build());
    }
}
