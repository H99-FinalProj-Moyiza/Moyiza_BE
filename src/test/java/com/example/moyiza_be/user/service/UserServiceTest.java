package com.example.moyiza_be.user.service;

import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import com.example.moyiza_be.user.util.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ValidationUtil validationUtil;
    @InjectMocks
    UserService userService;

    @Nested
    @DisplayName("SignUp")
    class SignUpUser {
        @Test
        @DisplayName("SUCCESS CASE")
        void signupUser_success() {
            // Given
            SignupRequestDto requestDto = new SignupRequestDto();
            requestDto.setEmail("test@example.com");
            requestDto.setPassword("password");
            requestDto.setNickname("nickname");
            requestDto.setImageUrl("");

            User user = new User();
            user.setEmail(requestDto.getEmail());
            user.setPassword(requestDto.getPassword());
            user.setNickname(requestDto.getNickname());

            when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
            doNothing().when(validationUtil).checkDuplicatedEmail(requestDto.getEmail());
            doNothing().when(validationUtil).checkDuplicatedNick(requestDto.getNickname());
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                savedUser.setId(1L);
                return savedUser;
            });

            // When
            userService.signup(requestDto);

            // Then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository, times(1)).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();

            assertEquals(requestDto.getEmail(), savedUser.getEmail());
            assertEquals("encodedPassword", savedUser.getPassword());
            assertEquals(requestDto.getNickname(), savedUser.getNickname());
        }
    }





}