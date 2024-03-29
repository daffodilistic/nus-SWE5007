package com.nus.project.capstone.model.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequests {

    // Request and Response
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
    private String userName; // for keycloak use only
    private Boolean isRegistered;
    private String userType;

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
                .isRegistered(u.getIsRegistered())
                .userType(u.getUserType())
                .build();
    }
}
