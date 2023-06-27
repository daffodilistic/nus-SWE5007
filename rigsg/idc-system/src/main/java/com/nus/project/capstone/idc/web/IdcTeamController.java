package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.idc.adapters.entity.IdcTeamRequests;
import com.nus.project.capstone.idc.adapters.persistence.IdcTeamJpaEntities;
import com.nus.project.capstone.idc.adapters.persistence.IdcTeamRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/idcteam")
public class IdcTeamController {

    private final IdcTeamRepository idcTeamRepository;

    @Autowired
    public IdcTeamController(IdcTeamRepository idcTeamRepository) {
        this.idcTeamRepository = idcTeamRepository;
    }

    @PostMapping("/team")
    public void createTeam(@RequestBody IdcTeamRequests idcTeamRequests) {

        idcTeamRepository.save(IdcTeamJpaEntities.toJpaEntity(idcTeamRequests));
    }

    @GetMapping("/team")
    public IdcTeamRequests readTeam(@RequestBody IdcTeamRequests idcTeamRequests) {

        val o = idcTeamRepository.findById(idcTeamRequests.getId());
        return o.map(IdcTeamRequests::toIdcTeamRequests).orElse(null);
    }

    @PutMapping("/team")
    public void updateTeam(@RequestBody IdcTeamRequests i) {

        if (i.getId() == null) {
            log.info("Idc team id must be provided");
            return;
        }

        if (idcTeamRepository.findById(i.getId()).isPresent()) {
            idcTeamRepository.save(IdcTeamJpaEntities.toJpaEntity(i));
        } else {
            log.info(String.format("Team %s is not found", i.getId()));
        }
    }

    @GetMapping("/teams")
    public List<IdcTeamJpaEntities> getAllUsers() {
        return idcTeamRepository.findAll();
    }
}
