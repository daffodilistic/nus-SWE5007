package com.nus.project.capstone.base.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserRequests;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.base.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    @Autowired
    private final UserRepository userRepository;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    private final String KEYCLOAK_USER_CREATION = "/admin/realms/ricsg/users";
    private final HashMap<String, String> dataMap = new HashMap<>();

    @Autowired
    public UserInfoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String getTestMessage() {
        return "Hello World @ UserInfoDao";
    }

    @PostMapping("/create-user")
    public ResponseEntity<GeneralMessageEntity> createUser(@RequestBody UserRequests userRequests,
                                                           @RequestHeader("Authorization") String bearerToken) {
        try{
            ResponseEntity<JsonNode> keycloakResponse = createUserInKeycloak(keycloakUrl + KEYCLOAK_USER_CREATION, bearerToken, userRequests, "participant");
            userRequests.setUserType("participants");
            val u = userRepository.save(UserJpaEntities.toJpaEntity(userRequests));
            return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data(e.toString()).build());
        }
    }

    @PostMapping("/create-judge")
    public ResponseEntity<GeneralMessageEntity> createJudge(@RequestBody UserRequests userRequests,
                                                           @RequestHeader("Authorization") String bearerToken) {
        try{
            ResponseEntity<JsonNode> keycloakResponse = createUserInKeycloak(keycloakUrl + KEYCLOAK_USER_CREATION, bearerToken, userRequests, "judge");
            userRequests.setUserType("judge");
            val u = userRepository.save(UserJpaEntities.toJpaEntity(userRequests));
            return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data(e.toString()).build());
        }
    }

    @PostMapping("/create-teacher")
    public ResponseEntity<GeneralMessageEntity> createTeacher(@RequestBody UserRequests userRequests,
                                                            @RequestHeader("Authorization") String bearerToken) {
        try{
            ResponseEntity<JsonNode> keycloakResponse = createUserInKeycloak(keycloakUrl + KEYCLOAK_USER_CREATION, bearerToken, userRequests, "participant");
            userRequests.setUserType("teacher");
            val u = userRepository.save(UserJpaEntities.toJpaEntity(userRequests));
            return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data(e.toString()).build());
        }
    }

    @PostMapping("/mark-attendance")
    public ResponseEntity<GeneralMessageEntity> markAttendance(@RequestBody UserRequests userRequests) {
        if (userRequests.getUserName() == null &&
            userRequests.getCountry()== null &&
            userRequests.getUserType() == null &&
            userRequests.getEmail() == null &&
            userRequests.getFirstName() == null &&
            userRequests.getLastName() == null &&
            userRequests.getDateOfBirth()== null &&
            userRequests.getPhoneNumber() == null &&
            userRequests.getSchoolName()== null &&
            userRequests.getState()== null
        ) {
            return updateUser(userRequests);
        } else {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data("Update failed. Request contains other illegal params. Pls check.").build());
        }
    }

    @PostMapping("/view-user")
    public ResponseEntity<GeneralMessageEntity> readUser(@RequestBody UserRequests userRequests) {

        val o = userRepository.findById(userRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(o.map(user -> UserResponse.toUserResponse(user, null)).orElse(null)).build());
    }

    @PostMapping("/view-user-email")
    public ResponseEntity<GeneralMessageEntity> readUserUsingEmail(@RequestBody UserRequests userRequests) {
        if (userRequests.getEmail() == null) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("User email must be provided").build());
        }
        val users = userRepository.findAll();
        List<UserJpaEntities> matchedUser = users.stream().filter(u -> u.getEmail().equals(userRequests.getEmail())).toList();
        if(matchedUser.size() != 1){
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data(String.format("No user email %s found in our database!", userRequests.getEmail())).build());
        }
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(matchedUser.get(0).getId()).build());
    }

    @PutMapping("/update-user")
    public ResponseEntity<GeneralMessageEntity> updateUser(@RequestBody UserRequests updateUserRequests) {

        if (updateUserRequests.getId() == null) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder().data("User id must be provided").build());
        }

        if (userRepository.findById(updateUserRequests.getId()).isEmpty()) {
            return ResponseEntity.badRequest().body(GeneralMessageEntity.builder()
                    .data(String.format("User %s is not found", updateUserRequests.getId())).build());
        }

        var user = userRepository.findById(updateUserRequests.getId()).get();
        user = user.updateJpaEntity(updateUserRequests);

        val u = userRepository.save(user);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
    }

    @GetMapping("/view-all-users")
    public ResponseEntity<GeneralMessageEntity> getAllUsers(@RequestHeader("Authorization") String bearerToken) {
        JsonNode usersInKeycloak = getUserInKeycloak(keycloakUrl + KEYCLOAK_USER_CREATION, bearerToken).getBody();
        for (JsonNode userNode : usersInKeycloak) {
            dataMap.put(String.valueOf(userNode.get("email")).replaceAll("\"", ""),
                    String.valueOf(userNode.get("username")).replaceAll("\"", ""));
        }
        val users = userRepository.findAll();
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(users.stream()
                .map(user -> UserResponse.toUserResponse(user, dataMap)).collect(Collectors.toList())).build());
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<GeneralMessageEntity> deleteUser(@RequestBody UserRequests userRequests) {
        userRepository.deleteById(userRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Delete success for %s", userRequests.getId())).build());
    }

    private ResponseEntity<JsonNode> createUserInKeycloak(String url, String token, UserRequests userRequests, String userType){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Authorization", token);
        HttpEntity<JsonNode> httpEntity = new HttpEntity<>(constructJsonForKeycloak(userRequests, userType), httpHeaders);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, JsonNode.class);
    }

    private ResponseEntity<JsonNode> getUserInKeycloak(String url, String token){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Authorization", token);
        HttpEntity<JsonNode> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, JsonNode.class);
    }

    private JsonNode constructJsonForKeycloak(UserRequests userRequests, String userType){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("enabled", true);
        List<String> groupList = new ArrayList<>();
        groupList.add(userType);
        jsonMap.put("groups", groupList);
        jsonMap.put("email", userRequests.getEmail());
        jsonMap.put("emailVerified", "");
        jsonMap.put("firstName", userRequests.getFirstName());
        jsonMap.put("lastName", userRequests.getLastName());
        jsonMap.put("username", userRequests.getUserName());
        return objectMapper.valueToTree(jsonMap);
    }
}
