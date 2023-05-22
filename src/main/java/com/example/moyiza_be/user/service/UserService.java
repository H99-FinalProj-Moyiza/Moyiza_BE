package com.example.moyiza_be.user.service;

import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public ResponseEntity<?> signup(SignupRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> findUserByEmail = userRepository.findByEmail(requestDto.getEmail());
        if (findUserByEmail.isPresent()) {
            throw new IllegalArgumentException("회원가입 중복된 이메일 사용");
        }

        Optional<User> findNicknameByEmail = userRepository.findByNickname(requestDto.getNickname());
        if (findNicknameByEmail.isPresent()) {
            throw new IllegalArgumentException("회원가입 중복된 닉네임 사용");
        }
        User user = new User(password, requestDto);
        userRepository.save(user);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

}
