package com.nus.project.capstone.idc.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.entity.base.UserRequests;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestsLocal extends UserRequests {
}
