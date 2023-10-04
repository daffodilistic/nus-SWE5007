package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.idc.service.GCPFileService;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserResponse;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nus.project.capstone.idc.service.GCPFileService.PRIM_FILE_SUFFIX;
import static com.nus.project.capstone.idc.service.GCPFileService.PROMO_FILE_SUFFIX;
import static com.nus.project.capstone.idc.web.Tools.genericFailureMessage;

@Slf4j
@RestController
@RequestMapping("/idcteam")
public class IdcTeamController {

    private final IdcTeamRepository idcTeamRepository;
    private final UserRepository userRepository;
    private final PresentationRepository presentationRepository;
    private final GCPFileService fileService;

    @Autowired
    public IdcTeamController(IdcTeamRepository idcTeamRepository, UserRepository userRepository,
                             PresentationRepository presentationRepository,
                             GCPFileService fileService) {
        this.idcTeamRepository = idcTeamRepository;
        this.userRepository = userRepository;
        this.presentationRepository = presentationRepository;
        this.fileService = fileService;
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
            return ResponseEntity.badRequest()
                    .body(GeneralMessageEntity.builder().data("Idc team id must be provided").build());
        }

        if (idcTeamRepository.findById(updateIdcTeamRequests.getId()).isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
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

    @PostMapping(value = "/upload-prim-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralMessageEntity> uploadPrimFile(@RequestParam("files") MultipartFile[] files,
                                                               @RequestParam("team") UUID idc_team_id) {
        MultipartFile file = files[0];
        String fileName = file.getOriginalFilename();
        if (fileName.substring(fileName.length() - 4).equals(PRIM_FILE_SUFFIX)) {
            var teamId = idcTeamRepository.findById(idc_team_id);
            if (teamId.isEmpty()) {
                return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                        .data(String.format("Team ID %s cannot be found", idc_team_id)).build());
            } else {
                return fileService.uploadFileToGCP(files[0], teamId.get().getTeamName());
            }
        } else {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data("Invalid file! must end with .pdf").build());
        }
    }

    @PostMapping(value = "/upload-promo-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralMessageEntity> uploadPromoFile(@RequestParam("files") MultipartFile[] files,
                                                                @RequestParam("team") UUID idc_team_id) {
        MultipartFile file = files[0];
        String fileName = file.getOriginalFilename();
        if (fileName.substring(fileName.length() - 4).equals(PROMO_FILE_SUFFIX)) {
            var teamId = idcTeamRepository.findById(idc_team_id);
            if (teamId.isEmpty()) {
                return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                        .data(String.format("Team ID %s cannot be found", idc_team_id)).build());
            } else {
                return fileService.uploadFileToGCP(files[0], teamId.get().getTeamName());
            }
        } else {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data("Invalid file! must end with .mp4").build());
        }
    }

    @PostMapping(value = "/download-file", produces = "application/octet-stream")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("file") String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");
        ByteArrayResource resource = fileService.downloadFileFromGCP(fileName);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_OCTET_STREAM).
                headers(headers).body(resource);
    }

    @GetMapping("/view-all-participants-files")
    public ResponseEntity<GeneralMessageEntity> getAllFiles() {
        List<String> fileNames = fileService.getAllDownloadableFiles(true);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(fileNames).build());
    }

    @GetMapping("/view-all-admin-files")
    public ResponseEntity<GeneralMessageEntity> getAllFiles2() {
        List<String> fileNames = fileService.getAllDownloadableFiles(false);
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(fileNames).build());
    }

    @PutMapping("/assign-score")
    public ResponseEntity<GeneralMessageEntity> assignScore(@RequestBody IdcTeamRequests idcTeamRequests) {
        if (idcTeamRequests.getPresentationRequestsList().size() != 1) {
            return genericFailureMessage();
        } else if (idcTeamRequests.getIsQualifiedPromo() == null &&
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

    @PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<GeneralMessageEntity> uploadFile(@RequestParam("files") MultipartFile[] files) {
        return fileService.uploadFileToGCP(files[0], "admin");
    }

}
