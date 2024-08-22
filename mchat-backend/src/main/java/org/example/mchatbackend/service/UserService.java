package org.example.mchatbackend.service;

import org.example.mchatbackend.dto.LoginRequest;
import org.example.mchatbackend.entity.Role;
import org.example.mchatbackend.entity.UserEntity;
import org.example.mchatbackend.repository.RoleRepository;
import org.example.mchatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public void addUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));;
        Role role = roleRepository.findByRoleName("USER").orElse(null);
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }
    public void updateUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRoleName("USER").orElse(null);
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<UserEntity> findAllUsers() {
       return userRepository.findAll();
    }
}
