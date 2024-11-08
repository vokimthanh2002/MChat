package org.example.mchatbackend.service;

import org.example.mchatbackend.entity.ChatRoom;
import org.example.mchatbackend.entity.Message;
import org.example.mchatbackend.repository.ChatRoomRepository;
import org.example.mchatbackend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }
    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }
    public List<Message> getMessagesByChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        return messageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }
}
