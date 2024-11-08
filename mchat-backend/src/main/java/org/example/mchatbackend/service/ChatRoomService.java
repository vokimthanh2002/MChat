package org.example.mchatbackend.service;

import org.example.mchatbackend.entity.ChatRoom;
import org.example.mchatbackend.entity.UserEntity;
import org.example.mchatbackend.repository.ChatRoomRepository;
import org.example.mchatbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;

//    public ChatRoom findOrCreateChatRoom(Long user1Id, Long user2Id) {
//        UserEntity user1 = userRepository.findById(user1Id).orElseThrow();
//        UserEntity user2 = userRepository.findById(user2Id).orElseThrow();
//
//        ChatRoom chatRoom = chatRoomRepository.findByUser1AndUser2(user1, user2);
//        if (chatRoom == null) {
//            chatRoom = chatRoomRepository.findByUser1AndUser2(user2, user1);
//        }
//
//        if (chatRoom == null) {
//            chatRoom = new ChatRoom();
//            chatRoom.setUser1(user1);
//            chatRoom.setUser2(user2);
//            chatRoomRepository.save(chatRoom);
//        }
//
//        return chatRoom;
//    }
    public ChatRoom findOrCreateChatRoom(String user1Username, String user2Username) {
        UserEntity user1 = userRepository.findByUsername(user1Username).orElseThrow();
        UserEntity user2 = userRepository.findByUsername(user2Username).orElseThrow();

        ChatRoom chatRoom = chatRoomRepository.findByUser1AndUser2(user1, user2);
        if (chatRoom == null) {
            chatRoom = chatRoomRepository.findByUser1AndUser2(user2, user1);
        }

        if (chatRoom == null) {
            chatRoom = new ChatRoom();
            chatRoom.setUser1(user1);
            chatRoom.setUser2(user2);
            chatRoomRepository.save(chatRoom);
        }

        return chatRoom;
    }
    public Optional<ChatRoom> finById(long id) {
       return chatRoomRepository.findById(id);
    }
}
