package com.example.moyiza_be.domain.user.repository;

import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    Optional<User> findByNicknameAndIsDeletedFalse(String nickname);
    Optional<User> findBySocialTypeAndSocialLoginIdAndIsDeletedFalse(SocialType socialType, String socialLoginId);
    Optional<User> findByNameAndPhoneAndIsDeletedFalse(String name, String phone);
    List<User> findByModifiedAtBeforeAndIsDeletedTrue(LocalDateTime localDateTime);
}
