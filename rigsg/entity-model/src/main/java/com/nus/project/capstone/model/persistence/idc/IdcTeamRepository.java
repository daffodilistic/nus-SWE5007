package com.nus.project.capstone.model.persistence.idc;

import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdcTeamRepository extends JpaRepository<IdcTeamJpaEntities, UUID> {

//    List<IdcTeamJpaEntities> findAllByTeamId(UUID teamId);
}
