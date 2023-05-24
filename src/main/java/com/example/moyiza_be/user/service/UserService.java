package com.example.moyiza_be.user.service;

import com.example.moyiza_be.common.security.jwt.JwtTokenDto;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshToken;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshTokenRepository;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.user.dto.LoginRequestDto;
import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.dto.UpdateRequestDto;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AwsS3Uploader awsS3Uploader;

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

    //로그인
    public ResponseEntity<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다."));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        JwtTokenDto tokenDto = jwtUtil.createAllToken(email);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(user.getEmail());
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user.getEmail());
            refreshTokenRepository.save(newToken);
        }
        setHeader(response, tokenDto);
        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

    //로그아웃
    public ResponseEntity<?> logout(HttpServletResponse response, String email) {
        refreshTokenRepository.deleteByEmail(email).orElseThrow(
                ()-> new NoSuchElementException("로그인한 사용자가 아닙니다."));
        return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
    }

    //회원정보 수정
    public ResponseEntity<?> updateProfile(MultipartFile imageFile, UpdateRequestDto requestDto, String email) {
        User foundUser = userRepository.findByEmail(email).orElseThrow(()->
                new NoSuchElementException("사용자가 존재하지 않습니다."));
        Optional<User> findNicknameByEmail = userRepository.findByNickname(requestDto.getNickname());
        if (findNicknameByEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 사용");
        }
        if(!imageFile.isEmpty()){
            awsS3Uploader.delete(foundUser.getProfileImage());
            String storedFileName  = awsS3Uploader.uploadFile(imageFile);
            foundUser.updateProfileImage(storedFileName);
        }
        foundUser.updateProfile(requestDto);

        return new ResponseEntity<>("회원정보 수정 완료", HttpStatus.OK);
    }

    private void setHeader(HttpServletResponse response, JwtTokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
