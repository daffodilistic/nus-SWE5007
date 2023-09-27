package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.entity.idc.*;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
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

import static com.nus.project.capstone.idc.web.Tools.genericFailureMessage;

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

    @PostMapping("/create-group")
    public ResponseEntity<GeneralMessageEntity> createGroup(@RequestBody IdcGroupRequests idcGroupRequests) {
        var group = idcGroupRepository.save(IdcGroupJpaEntities.toJpaEntity(idcGroupRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(group.getId()).build());
    }

    @PostMapping("/view-group")
    public ResponseEntity<GeneralMessageEntity> readGroup(@RequestBody IdcGroupRequests IdcGroupRequests) {

        val o = idcGroupRepository.findById(IdcGroupRequests.getId());
        val i = o.map(idcGroupJpaEntities -> {
            val idcGroupResponse = IdcGroupResponse.toIdcGroupResponse(idcGroupJpaEntities);
            idcGroupResponse.setIdcTeamResponses(idcGroupJpaEntities.getIdcTeams() == null ? null :
                    idcGroupJpaEntities.getIdcTeams().stream().distinct().map(IdcTeamResponse::toIdcTeamResponse)
                            .collect(Collectors.toList()));
            return idcGroupResponse;
        }).orElse(null);

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(i).build());
    }

    @PutMapping("/assign-team")
    public ResponseEntity<GeneralMessageEntity> assignTeamToGroup(@RequestBody IdcGroupRequests updateIdcGroupRequests) {
        if(updateIdcGroupRequests.getGroupName() == null){
            return updateGroup(updateIdcGroupRequests);
        } else {
            return genericFailureMessage();
        }
    }

    @PutMapping("/update-group")
    public ResponseEntity<GeneralMessageEntity> updateGroupInterface(@RequestBody IdcGroupRequests idcGroupRequests) {
        return updateGroup(idcGroupRequests);
    }

    private ResponseEntity<GeneralMessageEntity> updateGroup(@RequestBody IdcGroupRequests updateIdcGroupRequests) {

        if (updateIdcGroupRequests.getId() == null) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("Idc group id must be provided").build());
        }

        if (idcGroupRepository.findById(updateIdcGroupRequests.getId()).isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("IDC Group %s is not found", updateIdcGroupRequests.getId())).build());
        }

        var group = idcGroupRepository.findById(updateIdcGroupRequests.getId()).get();
        group = group.updateJpaEntity(updateIdcGroupRequests);

        if (updateIdcGroupRequests.getTeamIds() != null) {
            val originalTeamsInGroup = idcTeamRepository.findAll();
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

    @GetMapping("/view-all-groups")
    public ResponseEntity<GeneralMessageEntity> getAllGroups() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(idcGroupRepository.findAll().stream()
                .map(IdcGroupResponse::toIdcGroupResponse).collect(Collectors.toList())).build());
    }

    @DeleteMapping("/delete-group")
    public ResponseEntity<GeneralMessageEntity> deleteGroup(@RequestBody IdcGroupRequests idcGroupRequests) {
        idcGroupRepository.deleteById(idcGroupRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Delete success for %s", idcGroupRequests.getId())).build());
    }
}
