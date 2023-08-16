package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.idc.IdcMetricsRequests;
import com.nus.project.capstone.model.entity.idc.IdcMetricsResponse;
import com.nus.project.capstone.model.persistence.idc.IdcMetricsJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcMetricsRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/idcmetrics")
public class IdcMetricsController {

    private final IdcMetricsRepository idcMetricsRepository;

    @Autowired
    public IdcMetricsController(IdcMetricsRepository idcMetricsRepository) {
        this.idcMetricsRepository = idcMetricsRepository;
    }

    @PostMapping("/view-metric")
    public ResponseEntity<GeneralMessageEntity> readMetric(@RequestBody IdcMetricsRequests idcMetricsRequests) {

        val o = idcMetricsRepository.findById(idcMetricsRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(o.map(IdcMetricsResponse::toIdcMetricsResponse).orElse(null)).build());
    }

    @PostMapping("/create-metric")
    public ResponseEntity<GeneralMessageEntity> createMetric(@RequestBody IdcMetricsRequests idcMetricsRequests) {

        val u = idcMetricsRepository.save(IdcMetricsJpaEntities.toJpaEntity(idcMetricsRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
    }


    @PutMapping("/update-metric")
    public ResponseEntity<GeneralMessageEntity> updateMetric(@RequestBody IdcMetricsRequests idcMetricsRequests) {

        if (idcMetricsRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("IDC metric id must be provided").build());
        }

        if (idcMetricsRepository.findById(idcMetricsRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("IDC metric %s is not found", idcMetricsRequests.getId())).build());
        }

        var metric = idcMetricsRepository.findById(idcMetricsRequests.getId()).get();
        metric = metric.updateJpaEntity(idcMetricsRequests);

        val u = idcMetricsRepository.save(metric);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
    }

    @GetMapping("/view-all-metrics")
    public ResponseEntity<GeneralMessageEntity> getAllMetrics() {
        val metrics = idcMetricsRepository.findAll();
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(metrics.stream()
                .map(IdcMetricsResponse::toIdcMetricsResponse).collect(Collectors.toList())).build());
    }

    @PostMapping("/calculate")
    public ResponseEntity<GeneralMessageEntity> calculateScores(@RequestBody IdcMetricsRequests idcMetricsRequests) {
        float score = 0.0f;
        List<UUID> ids = idcMetricsRequests.getMetricIds();
        List<Float> scores = idcMetricsRequests.getMetricScores();
        if(ids != null && scores != null){
            for(int i = 0; i < ids.size(); i++){
                var metric = idcMetricsRepository.findById(ids.get(0)).get();
                score += metric.getMetricWeight() * scores.get(i);
            }
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(score).build());
        } else {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data("ids or scores are missing. Skip calculation").build());
        }
    }

    @DeleteMapping("/delete-metrics")
    public ResponseEntity<GeneralMessageEntity> deleteMetrics(@RequestBody IdcMetricsRequests idcMetricsRequests) {
        idcMetricsRepository.deleteById(idcMetricsRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Delete success for %s", idcMetricsRequests.getId())).build());
    }
}
