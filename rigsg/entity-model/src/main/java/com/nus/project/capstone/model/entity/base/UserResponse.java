package com.nus.project.capstone.model.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    // Response
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String state;
    private String dateOfBirth;
    private String phoneNumber;
    private String schoolName;
    private Integer yearsOfExp;
    private Boolean isQualified;
    private Boolean isQualifiedProm;
    private String team;

    public static UserResponse toUserResponse(UserJpaEntities u) {
        return UserResponse.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .country(u.getCountry())
                .state(u.getState())
                .dateOfBirth(u.getDateOfBirth())
                .phoneNumber(u.getPhoneNumber())
                .schoolName(u.getSchoolName())
                .yearsOfExp(u.getYearsOfExp())
                .isQualified(u.getIsQualified())
                .isQualifiedProm(u.getIsQualifiedProm())
                .team(u.getTeam() == null ? null : u.getTeam().getId().toString())
                .build();
    }
}
