package com.nus.project.capstone.model.persistence.idc;

import com.nus.project.capstone.model.entity.idc.IdcGroupRequests;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "idc_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IdcGroupJpaEntities {

    @Id
    @Column(nullable = false, name = "idc_group_id")
    private UUID id;
    private String groupName;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "idcGroup")
    private List<IdcTeamJpaEntities> idcTeams;

    public void addToIdcTeams(IdcTeamJpaEntities team) {
        team.setIdcGroup(this);
        idcTeams.add(team);
    }

    public static IdcGroupJpaEntities toJpaEntity(IdcGroupRequests g) {
        return IdcGroupJpaEntities.builder()
                .id(g.getId() == null ? UUID.randomUUID() : g.getId())
                .groupName(g.getGroupName())
                .build();
    }

    public IdcGroupJpaEntities updateJpaEntity(IdcGroupRequests g) {
        return IdcGroupJpaEntities.builder()
                .id(g.getId())
                .groupName(g.getGroupName() == null ? this.getGroupName() : g.getGroupName())
                .build();
    }
}
