package org.example.mchatbackend.controller;

import org.example.mchatbackend.entity.UserEntity;
import org.example.mchatbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @PostMapping(value = "/add-user")
    public ResponseEntity<?> addUserEntity(@RequestBody UserEntity userEntity) {
        if (userEntity != null) {
            userService.addUser(userEntity);
            return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
