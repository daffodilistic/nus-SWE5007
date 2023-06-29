package com.nus.project.capstone.base.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntities, UUID> {

    List<UserJpaEntities> findAllByTeamId(UUID teamId);
}
