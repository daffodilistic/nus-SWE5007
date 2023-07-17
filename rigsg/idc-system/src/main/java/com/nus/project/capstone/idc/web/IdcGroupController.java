package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.entity.idc.IdcGroupRequests;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserRequests;
import com.nus.project.capstone.model.entity.idc.IdcGroupResponse;
import com.nus.project.capstone.model.entity.idc.IdcTeamResponse;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcGroupJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcGroupRepository;
import com.nus.project.capstone.model.persistence.idc.IdcTeamRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/idcgroup")
public class IdcGroupController {

    private final IdcGroupRepository idcGroupRepository;
    private final IdcTeamRepository idcTeamRepository;

    @Autowired
    public IdcGroupController(IdcGroupRepository idcGroupRepository, IdcTeamRepository idcTeamRepository) {
        this.idcGroupRepository = idcGroupRepository;
        this.idcTeamRepository = idcTeamRepository;
    }

    @PostMapping("/group")
    public ResponseEntity<GeneralMessageEntity> createTeam(@RequestBody IdcGroupRequests idcGroupRequests) {
        var group = idcGroupRepository.save(IdcGroupJpaEntities.toJpaEntity(idcGroupRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(group.getId()).build());
    }

    @GetMapping("/group")
    public ResponseEntity<GeneralMessageEntity> readTeam(@RequestBody IdcGroupRequests IdcGroupRequests) {

        val o = idcGroupRepository.findById(IdcGroupRequests.getId());
        val i = o.map(idcGroupJpaEntities -> {
            val idcGroupResponse = IdcGroupResponse.toIdcGroupResponse(idcGroupJpaEntities);
            idcGroupResponse.setIdcTeamResponses(idcGroupJpaEntities.getIdcTeams() == null ? null :
                    idcGroupJpaEntities.getIdcTeams().stream().map(IdcTeamResponse::toIdcTeamResponse)
                            .collect(Collectors.toList()));
            return idcGroupResponse;
        }).orElse(null);

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(i).build());
    }

    @PutMapping("/group")
    public ResponseEntity<GeneralMessageEntity> updateTeam(@RequestBody IdcGroupRequests updateIdcGroupRequests) {

        if (updateIdcGroupRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Idc team id must be provided").build());
        }

        if (idcGroupRepository.findById(updateIdcGroupRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("Team %s is not found", updateIdcGroupRequests.getId())).build());
        }

        var group = idcGroupRepository.findById(updateIdcGroupRequests.getId()).get();
        group = group.updateJpaEntity(updateIdcGroupRequests);

        if (updateIdcGroupRequests.getTeamIds() != null) {
            val originalTeamsInGroup = idcTeamRepository.findAllById(updateIdcGroupRequests.getTeamIds());
            originalTeamsInGroup.forEach(t -> {
                t.setIdcGroup(null);
                idcTeamRepository.save(t);
            });

            val teams = idcTeamRepository.findAllById(updateIdcGroupRequests.getTeamIds());
            group.setIdcTeams(new ArrayList<>());
            teams.forEach(group::addToIdcTeams);
        }

        val g = idcGroupRepository.save(group);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(g.getId()).build());
    }

    @GetMapping("/groups")
    public ResponseEntity<GeneralMessageEntity> getAllUsers() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(idcGroupRepository.findAll().stream()
                .map(IdcGroupResponse::toIdcGroupResponse).collect(Collectors.toList())).build());
    }
}
