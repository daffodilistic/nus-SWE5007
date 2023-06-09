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

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String country;
    private String phoneNumber;
    private String isQualified;
    private String userId;

}
