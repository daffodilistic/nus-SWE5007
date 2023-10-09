package com.nus.project.capstone.model.persistence.base;

import com.nus.project.capstone.model.entity.base.UserRequests;
import com.nus.project.capstone.model.persistence.game.GameTeamJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserJpaEntities {

    @Id
    @Column(nullable = false, name = "user_id")
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
    private Boolean isRegistered;
    private String userType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idc_team_id", referencedColumnName = "idc_team_id")
    private IdcTeamJpaEntities idcTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_team_id", referencedColumnName = "game_team_id")
    private GameTeamJpaEntities gameTeam;

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
                .isRegistered(u.getIsRegistered() == null ? false: u.getIsRegistered())
                .userType(u.getUserType())
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
                .idcTeam(getIdcTeam())
                .gameTeam(getGameTeam())
                .isRegistered(u.getIsRegistered() == null ? getIsRegistered() : u.getIsRegistered())
                .userType(u.getUserType() == null ? getUserType() : u.getUserType())
                .build();
    }
}
