package com.nus.project.capstone.model.persistence.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameGroupRepository extends JpaRepository<GameGroupJpaEntities, UUID> {

}
