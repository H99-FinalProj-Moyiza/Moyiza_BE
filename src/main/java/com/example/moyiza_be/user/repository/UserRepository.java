package com.example.moyiza_be.user.repository;

import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findBySocialTypeAndSocialLoginId(SocialType socialType, String socialLoginId);
    Optional<User> findByNameAndPhone(String name, String phone);
    List<User> findByModifiedAtBeforeAndIsDeletedTrue(LocalDateTime localDateTime);
}
