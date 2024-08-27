package org.example.mchatbackend.repository;

import org.example.mchatbackend.entity.ChatRoom;
import org.example.mchatbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUser1AndUser2(UserEntity user1, UserEntity user2);
}
