package com.nus.project.capstone.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdcTeamRepository extends JpaRepository<IdcTeamJpaEntitiesLocal, UUID> {
}
