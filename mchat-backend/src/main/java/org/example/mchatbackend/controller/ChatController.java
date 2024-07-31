package org.example.mchatbackend.controller;

import org.example.mchatbackend.entity.Message;
import org.example.mchatbackend.entity.User;
import org.example.mchatbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Giả định đăng nhập thành công, không dùng Spring Security
        return ResponseEntity.ok(user);
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageService.save(message);
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageService.findAllMessages();
    }
}
