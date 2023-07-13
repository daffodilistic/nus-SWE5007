package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.idc.repository.*;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
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

    @PostMapping("/team")
    public ResponseEntity<GeneralMessageEntity> createTeam(@RequestBody IdcTeamRequestsLocal idcTeamRequests) {
        var team = idcTeamRepository.save((IdcTeamJpaEntitiesLocal)  IdcTeamJpaEntitiesLocal.toJpaEntity(idcTeamRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(team.getId()).build());
    }

    @GetMapping("/team")
    public ResponseEntity<GeneralMessageEntity> readTeam(@RequestBody IdcTeamRequestsLocal idcTeamRequests) {

        val o = idcTeamRepository.findById(idcTeamRequests.getId());
        val i = o.map(idcTeamJpa -> {
            val idcTeamResponse = IdcTeamResponseLocal.toIdcTeamResponse(idcTeamJpa);
            idcTeamResponse.setUserRequests(idcTeamJpa.getUsers() == null ? null :
                    idcTeamJpa.getUsers().stream().map(UserRequestsLocal::toUserRequests).collect(Collectors.toList()));
            return idcTeamResponse;
        }).orElse(null);

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(i).build());
    }

    @PutMapping("/team")
    public ResponseEntity<GeneralMessageEntity> updateTeam(@RequestBody IdcTeamRequestsLocal updateIdcTeamRequests) {

        if (updateIdcTeamRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Idc team id must be provided").build());
        }

        if (idcTeamRepository.findById(updateIdcTeamRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("Team %s is not found", updateIdcTeamRequests.getId())).build());
        }

        var team = idcTeamRepository.findById(updateIdcTeamRequests.getId()).get();
        team = (IdcTeamJpaEntitiesLocal) team.updateJpaEntity(updateIdcTeamRequests);

        if (updateIdcTeamRequests.getUserIds() != null) {
            List<UserJpaEntitiesLocal> originalUsersInTeam = userRepository.findAllByTeamId(team.getId());
            originalUsersInTeam.forEach(u -> {
                u.setTeam(null);
                userRepository.save(u);
            });

            val users = userRepository.findAllById(updateIdcTeamRequests.getUserIds());
            team.setUsers(new ArrayList<>());
            users.forEach(team::addToUsers);
        }

        val t = idcTeamRepository.save(team);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(t.getId()).build());
    }

    @GetMapping("/teams")
    public ResponseEntity<GeneralMessageEntity> getAllUsers() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(idcTeamRepository.findAll().stream()
                .map(IdcTeamResponseLocal::toIdcTeamResponse).collect(Collectors.toList())).build());
    }
}
