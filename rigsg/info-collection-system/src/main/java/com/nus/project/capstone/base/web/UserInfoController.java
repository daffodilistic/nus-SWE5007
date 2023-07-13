package com.nus.project.capstone.base.web;

import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import com.nus.project.capstone.model.entity.base.UserRequests;
import com.nus.project.capstone.model.entity.base.UserResponse;
import com.nus.project.capstone.model.persistence.base.UserJpaEntities;
import com.nus.project.capstone.model.persistence.base.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    private final UserRepository userRepository;

    @Autowired
    public UserInfoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String getTestMessage() {
        return "Hello World @ UserInfoDao";
    }

    @PostMapping("/qualifyParticipant")
    public void qualifyParticipant() {

    }

    @PostMapping("/user")
    public ResponseEntity<GeneralMessageEntity> createUser(@RequestBody UserRequests userRequests) {

        val u = userRepository.save(UserJpaEntities.toJpaEntity(userRequests));
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
    }

    @GetMapping("/user")
    public ResponseEntity<GeneralMessageEntity> readUser(@RequestBody UserRequests userRequests) {

        val o = userRepository.findById(userRequests.getId());
        return ResponseEntity.ok(GeneralMessageEntity.builder()
                .data(o.map(UserResponse::toUserResponse).orElse(null)).build());
    }

    @PutMapping("/user")
    public ResponseEntity<GeneralMessageEntity> updateUser(@RequestBody UserRequests updateUserRequests) {

        if (updateUserRequests.getId() == null) {
            return ResponseEntity.ok(GeneralMessageEntity.builder().data("User id must be provided").build());
        }

        if (userRepository.findById(updateUserRequests.getId()).isEmpty()) {
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(String.format("User %s is not found", updateUserRequests.getId())).build());
        }

        var user = userRepository.findById(updateUserRequests.getId()).get();
        user = user.updateJpaEntity(updateUserRequests);

        val u = userRepository.save(user);
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(u).build());
    }

    @GetMapping("/users")
    public ResponseEntity<GeneralMessageEntity> getAllUsers() {
        val users = userRepository.findAll();
        return ResponseEntity.ok(GeneralMessageEntity.builder().data(users.stream()
                .map(UserRequests::toUserRequests).collect(Collectors.toList())).build());
    }
}
