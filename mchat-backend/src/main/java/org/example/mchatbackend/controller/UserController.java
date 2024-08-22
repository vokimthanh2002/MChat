package org.example.mchatbackend.controller;

import org.example.mchatbackend.entity.UserEntity;
import org.example.mchatbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping(value = "/get-users")
    public ResponseEntity<?> getUsers() {
        List<UserEntity> users = userService.findAllUsers();
        if(users == null) {
            return null;
        }
        return ResponseEntity.ok(users);
    }
}
