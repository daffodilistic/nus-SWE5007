package com.nus.project.capstone.model.entity.idc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.idc.IdcMetricsJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdcMetricsResponse {

    private UUID id;
    private String stageName;
    private String metricName;
    private Float metricWeight;

    public static IdcMetricsResponse toIdcMetricsResponse(IdcMetricsJpaEntities i) {
        return IdcMetricsResponse.builder()
                .id(i.getId())
                .stageName(i.getStageName())
                .metricName(i.getMetricName())
                .metricWeight(i.getMetricWeight())
                .build();
    }
}
