package com.nus.project.capstone.base.adapters.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.base.adapters.persistence.UserJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequests {

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

    public static UserRequests toUserRequests(UserJpaEntities u) {
        return UserRequests.builder()
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
                .build();
    }
}
