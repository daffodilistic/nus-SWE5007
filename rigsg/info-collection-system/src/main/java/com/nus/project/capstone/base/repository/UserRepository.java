package com.nus.project.capstone.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntitiesLocal, UUID> {

    List<UserJpaEntitiesLocal> findAllByTeamId(UUID teamId);
}
