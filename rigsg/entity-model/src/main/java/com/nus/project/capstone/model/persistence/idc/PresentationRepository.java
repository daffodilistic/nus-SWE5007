package com.nus.project.capstone.model.persistence.idc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PresentationRepository extends JpaRepository<PresentationJpaEntities, UUID> {

    List<PresentationJpaEntities> findAllByIdcTeamId(UUID teamId);
}
