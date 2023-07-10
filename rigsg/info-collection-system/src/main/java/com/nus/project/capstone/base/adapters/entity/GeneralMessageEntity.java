package com.nus.project.capstone.base.adapters.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralMessageEntity {

    private Object data;
    @JsonProperty("trace_id")
    private String traceId;

    @Builder
    public GeneralMessageEntity(Object data) {
        this.data = data;
    }
}