package com.nus.project.capstone.model.persistence.idc;

import com.nus.project.capstone.model.entity.idc.IdcTeamRequests;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "idc_team")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IdcTeamJpaEntities {

    @Id
    @Column(nullable = false, name = "idc_team_id")
    private UUID id;
    private String teamName;
    private String ageGroup;
    private Integer rankFirstStage;
    private Boolean isQualifiedPromo;
    private Boolean isQualifiedFinal;
    private Boolean isQualifiedFinalSecondStage;
    private UUID teacherId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "idcTeam")
    private List<UserJpaEntities> users;

    //join column. name is fk, referenced column name is the pk column id on other table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idc_group_id", referencedColumnName = "idc_group_id")
    private IdcGroupJpaEntities idcGroup;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "idcTeam", cascade = CascadeType.PERSIST)
    private Set<PresentationJpaEntities> presentations;

    public void addToUsers(UserJpaEntities user) {
        user.setIdcTeam(this);
        users.add(user);
    }

    public static IdcTeamJpaEntities toJpaEntity(IdcTeamRequests i) {
        return IdcTeamJpaEntities.builder()
                .id(i.getId() == null ? UUID.randomUUID() : i.getId())
                .teamName(i.getTeamName())
                .ageGroup(i.getAgeGroup())
                .rankFirstStage(i.getRankFirstStage())
                .isQualifiedPromo(i.getIsQualifiedPromo())
                .isQualifiedFinal(i.getIsQualifiedFinal())
                .isQualifiedFinalSecondStage(i.getIsQualifiedFinalSecondStage())
                .teacherId(i.getTeacherId())
                .build();
    }

    public IdcTeamJpaEntities updateJpaEntity(IdcTeamRequests u) {
        return IdcTeamJpaEntities.builder()
                .id(u.getId())
                .teamName(u.getTeamName() == null ? this.getTeamName() : u.getTeamName())
                .ageGroup(u.getAgeGroup() == null ? this.getAgeGroup() : u.getAgeGroup())
                .rankFirstStage(u.getRankFirstStage() == null ? this.getRankFirstStage()
                        : u.getRankFirstStage())
                .isQualifiedPromo(u.getIsQualifiedPromo() == null ? this.getIsQualifiedPromo()
                        : u.getIsQualifiedPromo())
                .isQualifiedFinal(u.getIsQualifiedFinal() == null ? this.getIsQualifiedFinal()
                        : u.getIsQualifiedFinal())
                .isQualifiedFinalSecondStage(u.getIsQualifiedFinalSecondStage() == null ? this.getIsQualifiedFinalSecondStage()
                        : u.getIsQualifiedFinalSecondStage())
                .teacherId(u.getTeacherId() == null ? this.getTeacherId()
                        : u.getTeacherId())
                .build();
    }
}
