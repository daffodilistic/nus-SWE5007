package com.nus.project.capstone.model.persistence.idc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdcMetricsRepository extends JpaRepository<IdcMetricsJpaEntities, UUID> {

}
