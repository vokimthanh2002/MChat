package com.example.chat.chat;

import org.example.mchatbackend.controller.ChatMessage;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage (
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket sesseion
        headerAccessor.getSessionAttributes().put("user", chatMessage);
        return chatMessage;
    }
}
