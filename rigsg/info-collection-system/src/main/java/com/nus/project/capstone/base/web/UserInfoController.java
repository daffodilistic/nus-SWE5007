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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            ResponseEntity<JsonNode> keycloakResponse = createUserInKeycloak(keycloakUrl + KEYCLOAK_USER_CREATION, bearerToken, userRequests);
            val u = userRepository.save(UserJpaEntities.toJpaEntity(userRequests));
            return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
        } catch (Exception e){
            return ResponseEntity.ok(GeneralMessageEntity.builder().data(e.toString()).build());
        }
    }

    @PostMapping("/view-user")
    public ResponseEntity<GeneralMessageEntity> readUser(@RequestBody UserRequests userRequests) {

        val o = userRepository.findById(userRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(o.map(UserResponse::toUserResponse).orElse(null)).build());
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
    public ResponseEntity<GeneralMessageEntity> getAllUsers() {
        val users = userRepository.findAll();
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(users.stream()
                .map(UserResponse::toUserResponse).collect(Collectors.toList())).build());
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<GeneralMessageEntity> deleteUser(@RequestBody UserRequests userRequests) {
        userRepository.deleteById(userRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(String.format("Delete success for %s", userRequests.getId())).build());
    }

    private ResponseEntity<JsonNode> createUserInKeycloak(String url, String token, UserRequests userRequests){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Authorization", token);
        HttpEntity<JsonNode> httpEntity = new HttpEntity<>(constructJsonForKeycloak(userRequests), httpHeaders);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, JsonNode.class);
    }

    private JsonNode constructJsonForKeycloak(UserRequests userRequests){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("enabled", true);
        List<String> groupList = new ArrayList<>();
        groupList.add("participant");
        jsonMap.put("groups", groupList);
        jsonMap.put("email", userRequests.getEmail());
        jsonMap.put("emailVerified", "");
        jsonMap.put("firstName", userRequests.getFirstName());
        jsonMap.put("lastName", userRequests.getLastName());
        jsonMap.put("username", userRequests.getUserName());
        return objectMapper.valueToTree(jsonMap);
    }
}
