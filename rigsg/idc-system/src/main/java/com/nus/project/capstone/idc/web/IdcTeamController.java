package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.idc.service.GCPFileUploadService;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.entity.idc.FileUploadRequests;
import com.nus.project.capstone.model.entity.idc.IdcTeamRequests;
import com.nus.project.capstone.model.entity.idc.IdcTeamResponse;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.base.UserRepository;
import com.nus.project.capstone.model.persistence.idc.IdcTeamJpaEntities;
import com.nus.project.capstone.model.persistence.idc.IdcTeamRepository;
import com.nus.project.capstone.model.persistence.idc.PresentationJpaEntities;
import com.nus.project.capstone.model.persistence.idc.PresentationRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.nus.project.capstone.idc.service.GCPFileUploadService.PRIM_FILE_SUFFIX;
import static com.nus.project.capstone.idc.service.GCPFileUploadService.PROMO_FILE_SUFFIX;
import static com.nus.project.capstone.idc.web.Tools.genericFailureMessage;

@Slf4j
@RestController
@RequestMapping("/idcteam")
public class IdcTeamController {

    private final IdcTeamRepository idcTeamRepository;
    private final UserRepository userRepository;
    private final PresentationRepository presentationRepository;
    private final GCPFileUploadService fileUploadService;

    @Autowired
    public IdcTeamController(IdcTeamRepository idcTeamRepository, UserRepository userRepository,
                             PresentationRepository presentationRepository,
                             GCPFileUploadService fileUploadService) {
        this.idcTeamRepository = idcTeamRepository;
        this.userRepository = userRepository;
        this.presentationRepository = presentationRepository;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/create-team")
    public ResponseEntity<GeneralMessageEntity> createTeam(@RequestBody IdcTeamRequests idcTeamRequests) {
        var team = idcTeamRepository.save(IdcTeamJpaEntities.toJpaEntity(idcTeamRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(team.getId()).build());
    }

    @PostMapping("/view-team")
    public ResponseEntity<GeneralMessageEntity> readTeam(@RequestBody IdcTeamRequests idcTeamRequests) {

        val o = idcTeamRepository.findById(idcTeamRequests.getId());
        val i = o.map(idcTeamJpa -> {
            val idcTeamResponse = IdcTeamResponse.toIdcTeamResponse(idcTeamJpa);
            idcTeamResponse.setUserResponses(idcTeamJpa.getUsers() == null ? null :
                    idcTeamJpa.getUsers().stream().map(UserResponse::toUserResponse).collect(Collectors.toList()));
            return idcTeamResponse;
        }).orElse(null);

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(i).build());
    }

    @PutMapping("/qualify-team")
    public ResponseEntity<GeneralMessageEntity> qualifyTeam(@RequestBody IdcTeamRequests idcTeamRequests) {
        if (idcTeamRequests.getTeamName() == null &&
                idcTeamRequests.getIdcGroupId() == null &&
                idcTeamRequests.getAgeGroup() == null &&
                idcTeamRequests.getRankFirstStage() == null &&
                idcTeamRequests.getTeacherId() == null &&
                idcTeamRequests.getUserIds() == null) {
            return updateTeam(idcTeamRequests);
        } else {
            return genericFailureMessage();
        }
    }

    @PutMapping("/assign-user")
    public ResponseEntity<GeneralMessageEntity> assignUser(@RequestBody IdcTeamRequests idcTeamRequests) {
        if (idcTeamRequests.getIsQualifiedPromo() == null &&
                idcTeamRequests.getIsQualifiedFinal() == null &&
                idcTeamRequests.getIsQualifiedFinalSecondStage() == null &&
                idcTeamRequests.getIdcGroupId() == null &&
                idcTeamRequests.getAgeGroup() == null &&
                idcTeamRequests.getRankFirstStage() == null &&
                idcTeamRequests.getTeacherId() == null) {
            return updateTeam(idcTeamRequests);
        } else {
            return genericFailureMessage();
        }
    }

    @PutMapping("/update-team")
    public ResponseEntity<GeneralMessageEntity> updateTeamInterface(@RequestBody IdcTeamRequests idcTeamRequests) {
        return updateTeam(idcTeamRequests);
    }

    private ResponseEntity<GeneralMessageEntity> updateTeam(IdcTeamRequests updateIdcTeamRequests) {

        if (updateIdcTeamRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("Idc team id must be provided").build());
        }

        if (idcTeamRepository.findById(updateIdcTeamRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("IDC Team %s is not found", updateIdcTeamRequests.getId())).build());
        }

        var team = idcTeamRepository.findById(updateIdcTeamRequests.getId()).get();
        team = team.updateJpaEntity(updateIdcTeamRequests);

        if (updateIdcTeamRequests.getUserIds() != null) {
            List<UserJpaEntities> originalUsersInTeam = userRepository.findAllByIdcTeamId(team.getId());
            originalUsersInTeam.forEach(u -> {
                u.setIdcTeam(null);
                userRepository.save(u);
            });

            val users = userRepository.findAllById(updateIdcTeamRequests.getUserIds());
            team.setUsers(new ArrayList<>());
            users.forEach(team::addToUsers);
        }

        if (updateIdcTeamRequests.getPresentationRequestsList() != null) {

            val allPres = presentationRepository.findAllByIdcTeamId(team.getId());
            allPres.forEach(ps -> {
                ps.setIdcTeam(null);
                presentationRepository.save(ps);
            });

            val p = updateIdcTeamRequests.getPresentationRequestsList().stream()
                    .map(PresentationJpaEntities::toJpaEntity).collect(Collectors.toSet());

            val teamFinal = team;
            p.forEach(pres -> pres.setIdcTeam(teamFinal));

            presentationRepository.saveAll(p);
            team.setPresentations(p);
        }

        val t = idcTeamRepository.save(team);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(t.getId()).build());
    }

    @GetMapping("/view-all-teams")
    public ResponseEntity<GeneralMessageEntity> getAllTeams() {

        return ResponseEntity.ok(GeneralMessageEntity.builder().data(idcTeamRepository.findAll().stream()
                .map(IdcTeamResponse::toIdcTeamResponse).collect(Collectors.toList())).build());
    }

    @DeleteMapping("/delete-team")
    public ResponseEntity<GeneralMessageEntity> deleteTeam(@RequestBody IdcTeamRequests idcTeamRequests) {
        idcTeamRepository.deleteById(idcTeamRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Delete success for %s", idcTeamRequests.getId())).build());
    }

    @PostMapping("/upload-prim-file")
    public ResponseEntity<GeneralMessageEntity> uploadPrimFile(@RequestBody FileUploadRequests fileUploadRequests) {
        if (fileUploadRequests.getLocalFilePath().substring(fileUploadRequests.getLocalFilePath().length() - 4).equals(PRIM_FILE_SUFFIX)){
            var teamId = idcTeamRepository.findById(fileUploadRequests.getIdcTeamId());
            if(teamId.isEmpty()){
                return ResponseEntity.ok(GeneralMessageEntity.builder()
                        .data(String.format("Team ID %s cannot be found", fileUploadRequests.getIdcTeamId())).build());
            } else {
                return fileUploadService.uploadFileToGCP(fileUploadRequests.getLocalFilePath(), teamId.get().getTeamName());
            }
        } else {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data("Invalid file! must end with .pdf").build());
        }
    }

    @PostMapping("/upload-promo-file")
    public ResponseEntity<GeneralMessageEntity> uploadPromoFile(@RequestBody FileUploadRequests fileUploadRequests) {
        if (fileUploadRequests.getLocalFilePath().substring(fileUploadRequests.getLocalFilePath().length() - 4).equals(PROMO_FILE_SUFFIX)){
            var teamId = idcTeamRepository.findById(fileUploadRequests.getIdcTeamId());
            if(teamId.isEmpty()){
                return ResponseEntity.ok(GeneralMessageEntity.builder()
                        .data(String.format("Team ID %s cannot be found", fileUploadRequests.getIdcTeamId())).build());
            } else {
                return fileUploadService.uploadFileToGCP(fileUploadRequests.getLocalFilePath(), teamId.get().getTeamName());
            }
        } else {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data("Invalid file! must end with .mp4").build());
        }
    }

}
