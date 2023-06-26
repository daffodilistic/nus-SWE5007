package com.nus.project.capstone.base.adapters.persistence;

import com.nus.project.capstone.base.adapters.entity.UserRequests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserJpaEntities {

    @Id
    @Column(nullable = false)
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
    private UUID teamId;

    public static UserJpaEntities toJpaEntity(UserRequests u) {
        return UserJpaEntities.builder()
                .id(u.getId() == null ? UUID.randomUUID() : u.getId())
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
