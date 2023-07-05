package com.nus.project.capstone.base.web;

import com.nus.project.capstone.base.adapters.entity.IdcTeamRequests;
import com.nus.project.capstone.base.adapters.entity.UserRequests;
import com.nus.project.capstone.base.adapters.persistence.IdcTeamJpaEntities;
import com.nus.project.capstone.base.adapters.persistence.IdcTeamRepository;
import com.nus.project.capstone.base.adapters.persistence.UserJpaEntities;
import com.nus.project.capstone.base.adapters.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
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
    public void createTeam(@RequestBody IdcTeamRequests idcTeamRequests) {

        idcTeamRepository.save(IdcTeamJpaEntities.toJpaEntity(idcTeamRequests));
    }

    @GetMapping("/team")
    public IdcTeamRequests readTeam(@RequestBody IdcTeamRequests idcTeamRequests) {

        val o = idcTeamRepository.findById(idcTeamRequests.getId());
        return o.map(idcTeamJpa -> {
            val idcTeamReq = IdcTeamRequests.toIdcTeamRequests(idcTeamJpa);
            idcTeamReq.setUserRequests(idcTeamJpa.getUsers() == null ? null : idcTeamJpa.getUsers().stream().map(UserRequests::toUserRequests).collect(Collectors.toList()));
            return idcTeamReq;
        }).orElse(null);
    }

    @PutMapping("/team")
    public void updateTeam(@RequestBody IdcTeamRequests updateIdcTeamRequests) {

        if (updateIdcTeamRequests.getId() == null) {
            log.info("Idc team id must be provided");
            return;
        }

        if (idcTeamRepository.findById(updateIdcTeamRequests.getId()).isEmpty()) {
            log.info(String.format("Team %s is not found", updateIdcTeamRequests.getId()));
            return;
        }

        var team = idcTeamRepository.findById(updateIdcTeamRequests.getId()).get();
        team = team.updateJpaEntity(updateIdcTeamRequests);

        if (updateIdcTeamRequests.getUserIds() != null) {
            List<UserJpaEntities> originalUsersInTeam = userRepository.findAllByTeamId(team.getId());
            originalUsersInTeam.forEach(u -> {
                u.setTeam(null);
                userRepository.save(u);
            });

            val users = userRepository.findAllById(updateIdcTeamRequests.getUserIds());
            team.setUsers(new ArrayList<>());
            users.forEach(team::addToUsers);
        }

        idcTeamRepository.save(team);
    }

    @GetMapping("/teams")
    public List<IdcTeamRequests> getAllUsers() {
        return idcTeamRepository.findAll().stream().map(IdcTeamRequests::toIdcTeamRequests).collect(Collectors.toList());
    }
}
