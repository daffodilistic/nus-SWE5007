package com.nus.project.capstone.rigsg.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdcTeamRepository extends JpaRepository<IdcTeamJpaEntities, UUID> {
}
