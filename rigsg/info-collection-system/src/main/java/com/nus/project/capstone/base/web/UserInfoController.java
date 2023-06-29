package com.nus.project.capstone.base.web;

import com.nus.project.capstone.base.adapters.entity.UserRequests;
import com.nus.project.capstone.base.adapters.persistence.UserJpaEntities;
import com.nus.project.capstone.base.adapters.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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
    public void createUser(@RequestBody UserRequests userRequests) {

        userRepository.save(UserJpaEntities.toJpaEntity(userRequests));
    }

    @GetMapping("/user")
    public UserRequests readUser(@RequestBody UserRequests userRequests) {

        val o = userRepository.findById(userRequests.getId());
        return o.map(UserRequests::toUserRequests).orElse(null);
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody UserRequests updateUserRequests) {

        if (updateUserRequests.getId() == null) {
            log.info("User id must be provided");
            return;
        }

        if (userRepository.findById(updateUserRequests.getId()).isEmpty()) {
            log.info(String.format("User %s is not found", updateUserRequests.getId()));
            return;
        }

        var user = userRepository.findById(updateUserRequests.getId()).get();
        user = user.updateJpaEntity(updateUserRequests);

        userRepository.save(user);
    }

    @GetMapping("/users")
    public List<UserRequests> getAllUsers() {
        val users = userRepository.findAll();
        return users.stream().map(UserRequests::toUserRequests).collect(Collectors.toList());
    }
}
