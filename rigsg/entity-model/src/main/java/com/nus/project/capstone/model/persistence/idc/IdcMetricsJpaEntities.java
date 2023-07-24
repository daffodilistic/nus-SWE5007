package com.nus.project.capstone.model.persistence.idc;

import com.nus.project.capstone.model.entity.idc.IdcMetricsRequests;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "scoring_metric")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IdcMetricsJpaEntities {

    @Id
    @Column(nullable = false, name = "scoring_metric_id")
    private UUID id;
    private String stageName;
    private String metricName;
    private Float metricWeight;

    public static IdcMetricsJpaEntities toJpaEntity(IdcMetricsRequests i) {
        return IdcMetricsJpaEntities.builder()
                .id(i.getId() == null ? UUID.randomUUID() : i.getId())
                .stageName(i.getStageName())
                .metricName(i.getMetricName())
                .metricWeight(i.getMetricWeight())
                .build();
    }

    public IdcMetricsJpaEntities updateJpaEntity(IdcMetricsRequests u) {
        return IdcMetricsJpaEntities.builder()
                .id(u.getId())
                .stageName(u.getStageName() == null ? this.getStageName() : u.getStageName())
                .metricName(u.getMetricName() == null ? this.getMetricName() : u.getMetricName())
                .metricWeight(u.getMetricWeight() == null ? this.getMetricWeight() : u.getMetricWeight())
                .build();
    }
}
