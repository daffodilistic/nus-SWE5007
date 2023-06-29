package com.nus.project.capstone.base.adapters.persistence;

import com.nus.project.capstone.base.adapters.entity.UserRequests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Boolean isQualifiedProm;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private IdcTeamJpaEntities team;

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
                .isQualifiedProm(u.getIsQualifiedProm())
                .build();
    }

    public UserJpaEntities updateJpaEntity(UserRequests u) {
        return UserJpaEntities.builder()
                .id(u.getId())
                .firstName(u.getFirstName() == null ? getFirstName() : u.getFirstName())
                .lastName(u.getLastName() == null ? getLastName() : u.getLastName())
                .email(u.getEmail() == null ? getEmail() : u.getEmail())
                .country(u.getCountry() == null ? getCountry() : u.getCountry())
                .state(u.getState() == null ? getState() : u.getState())
                .dateOfBirth(u.getDateOfBirth() == null ? getDateOfBirth() : u.getDateOfBirth())
                .phoneNumber(u.getPhoneNumber() == null ? getPhoneNumber() : u.getPhoneNumber())
                .schoolName(u.getSchoolName() == null ? getSchoolName() : u.getSchoolName())
                .yearsOfExp(u.getYearsOfExp() == null ? getYearsOfExp() : u.getYearsOfExp())
                .isQualified(u.getIsQualified() == null ? getIsQualified() : u.getIsQualified())
                .isQualifiedProm(u.getIsQualifiedProm() == null ? getIsQualifiedProm() : u.getIsQualifiedProm())
                .team(getTeam())
                .build();
    }
}
