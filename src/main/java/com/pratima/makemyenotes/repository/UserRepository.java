package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastactiveat = :lastActive WHERE u.email = :email")
    void updateLastActive(@Param("email") String email,
                          @Param("lastActive") LocalDateTime lastActive);

    // Find all online users (active in last 5 minutes)
    @Query("SELECT u FROM User u WHERE u.lastactiveat > :threshold")
    List<User> findOnlineUsers(@Param("threshold") LocalDateTime threshold);
}
