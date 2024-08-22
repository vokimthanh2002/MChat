package org.example.mchatbackend.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.mchatbackend.dto.AuthReposeDTO;
import org.example.mchatbackend.dto.LoginRequest;
import org.example.mchatbackend.dto.RegisterRequest;
import org.example.mchatbackend.entity.LoginLog;
import org.example.mchatbackend.entity.Role;
import org.example.mchatbackend.entity.UserEntity;
import org.example.mchatbackend.repository.LoginLogRepository;
import org.example.mchatbackend.repository.RoleRepository;
import org.example.mchatbackend.repository.UserRepository;
import org.example.mchatbackend.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(value = "api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;
    @Autowired
    private LoginLogRepository loginLogRepository;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtGenerator generator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = generator;
    }


    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello World";
    }
    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.OK);
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return new ResponseEntity<>("User name is takes", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Role role = roleRepository.findByRoleName("USER").orElse(null);
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        // Kiểm tra số lượng tài khoản đã đăng nhập từ IP này
        int distinctUsersCount = loginLogRepository.countDistinctUsernamesByIpAddress(httpServletRequest.getRemoteAddr());

        if (distinctUsersCount >= 3) {
            return new ResponseEntity<>("IP này đã đăng nhập quá 3 tài khoản khác nhau!", HttpStatus.FORBIDDEN);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        // Lấy địa chỉ IP từ request
        String ipAddress = httpServletRequest.getRemoteAddr();
        // Lưu vết đăng nhập
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(loginRequest.getUsername());
        loginLog.setLoginTime(LocalDateTime.now());
        loginLog.setIpAddress(ipAddress);
        loginLogRepository.save(loginLog);

        return new ResponseEntity<>(new AuthReposeDTO(token, loginRequest.getUsername()), HttpStatus.OK);
    }
}
