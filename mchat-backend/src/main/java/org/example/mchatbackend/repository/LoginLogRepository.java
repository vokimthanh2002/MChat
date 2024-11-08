package org.example.mchatbackend.repository;

import org.example.mchatbackend.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    @Query("SELECT COUNT(DISTINCT l.username) FROM LoginLog l WHERE l.ipAddress = :ipAddress")
    int countDistinctUsernamesByIpAddress(@Param("ipAddress") String ipAddress);
}
