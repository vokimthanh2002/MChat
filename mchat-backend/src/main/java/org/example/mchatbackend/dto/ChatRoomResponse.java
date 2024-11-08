package org.example.mchatbackend.dto;

import org.example.mchatbackend.entity.Message;

import java.util.List;

public class ChatRoomResponse {
    private Long chatRoomId;
    private List<Message> messages;

    public ChatRoomResponse(Long chatRoomId, List<Message> messages) {
        this.chatRoomId = chatRoomId;
        this.messages = messages;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
