package org.example.mchatbackend.controller;

import org.example.mchatbackend.dto.ChatRoomResponse;
import org.example.mchatbackend.entity.ChatRoom;
import org.example.mchatbackend.entity.Message;
import org.example.mchatbackend.service.ChatRoomService;
import org.example.mchatbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageService.save(message);
    }
    @GetMapping("/room")
    public ResponseEntity<ChatRoomResponse> getChatRoom(
            @RequestParam String user1Username,
            @RequestParam String user2Username) {

        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(user1Username, user2Username);
        List<Message> messages = messageService.getMessagesByChatRoom(chatRoom.getId());

        ChatRoomResponse response = new ChatRoomResponse(chatRoom.getId(), messages);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-chatroom")
    public ResponseEntity<ChatRoom> getChatRoomById(@RequestParam Long idRoom){
        return ResponseEntity.ok(chatRoomService.finById(idRoom).orElseThrow());
    }
}
