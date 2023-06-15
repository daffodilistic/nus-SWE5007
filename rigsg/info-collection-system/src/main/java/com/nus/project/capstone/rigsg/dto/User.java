package com.nus.project.capstone.rigsg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String state;
    private String dateOfBirth;
    private String phoneNumber;
    private String schoolName;
    private String yearOfExp;
    private String isQualified;
    private String teamId;

}
