package com.example.moyiza_be.user.repository;

import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findBySocialTypeAndSocialLoginId(SocialType socialType, String socialLoginId);
}
