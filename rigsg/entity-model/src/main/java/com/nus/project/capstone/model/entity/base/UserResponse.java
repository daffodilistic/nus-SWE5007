package com.nus.project.capstone.model.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
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
    private String idcTeam;
    private String gameTeam;
    private Boolean isRegistered;
    private String userType;
    private String userName; // only used for keycloak

    public static UserResponse toUserResponse(UserJpaEntities u, HashMap<String, String> data) {
        UserResponseBuilder builder =  UserResponse.builder()
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
                .idcTeam(u.getIdcTeam() == null ? null : u.getIdcTeam().getId().toString())
                .gameTeam(u.getGameTeam() == null ? null : u.getGameTeam().getId().toString())
                .isRegistered(u.getIsRegistered())
                .userType(u.getUserType());
        if(data != null){
            return builder.userName(data.getOrDefault(u.getEmail(), "")).build();
        }
        return builder.build();
    }
}
