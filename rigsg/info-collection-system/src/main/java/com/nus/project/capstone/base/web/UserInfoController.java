package com.nus.project.capstone.base.web;

import com.nus.project.capstone.base.adapters.entity.UserRequests;
import com.nus.project.capstone.base.adapters.persistence.UserJpaEntities;
import com.nus.project.capstone.base.adapters.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void updateUser(@RequestBody UserRequests u) {

        if (u.getId() == null) {
            log.info("User id must be provided");
            return;
        }

        if (userRepository.findById(u.getId()).isPresent()) {
            userRepository.save(UserJpaEntities.toJpaEntity(u));
        } else {
            log.info(String.format("User %s is not found", u.getId()));
        }
    }

    @GetMapping("/users")
    public List<UserJpaEntities> getAllUsers() {
        return userRepository.findAll();
    }
}
