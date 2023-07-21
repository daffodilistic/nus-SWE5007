package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.idc.IdcMetricsRequests;
import com.nus.project.capstone.model.entity.idc.IdcMetricsResponse;
import com.nus.project.capstone.model.persistence.idc.IdcMetricsRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/idcmetrics")
public class IdcMetricsController {

    private final IdcMetricsRepository idcMetricsRepository;

    @Autowired
    public IdcMetricsController(IdcMetricsRepository idcMetricsRepository) {
        this.idcMetricsRepository = idcMetricsRepository;
    }

    @GetMapping("/metric")
    public ResponseEntity<GeneralMessageEntity> readMetric(@RequestBody IdcMetricsRequests idcMetricsRequests) {

        val o = idcMetricsRepository.findById(idcMetricsRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(o.map(IdcMetricsResponse::toIdcMetricsResponse).orElse(null)).build());
    }
}
