package com.example.moyiza_be.user.service;

import com.example.moyiza_be.club.dto.ClubListOnMyPage;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.common.enums.BasicProfileEnum;
import com.example.moyiza_be.common.security.jwt.CookieUtil;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshTokenRepository;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.oneday.service.OneDayService;
import com.example.moyiza_be.user.dto.*;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ClubService clubService;

    public ResponseEntity<?> signup(SignupRequestDto requestDto, MultipartFile imageFile) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        String storedFileUrl = BasicProfileEnum.getRandomImage().getImageUrl();
        checkDuplicatedEmail(requestDto.getEmail());
        checkDuplicatedNick(requestDto.getNickname());
        if(imageFile != null){
            storedFileUrl  = awsS3Uploader.uploadFile(imageFile);
        }
        User user = new User(password, requestDto, storedFileUrl);
        user.authorizeUser();
        userRepository.save(user);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }
    public ResponseEntity<?> updateSocialInfo(UpdateSocialInfoRequestDto requestDto, User user) {
        User foundUser = findUser(user.getEmail());
        checkDuplicatedNick(requestDto.getNickname());
        foundUser.updateSocialInfo(requestDto);
        foundUser.authorizeUser();
        return new ResponseEntity<>("Social signup complete! ", HttpStatus.OK);
    }
    public ResponseEntity<?> getSocialInfo(User user) {
//        User foundUser = findUser(user.getEmail());
        SocialInfoResponseDto responseDto = new SocialInfoResponseDto(user.getName(), user.getNickname());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //Login
    public ResponseEntity<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        User user = findUser(email);
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Invalid password.");
        }
        jwtUtil.createAndSetToken(response, user);
        return new ResponseEntity<>("Successful login", HttpStatus.OK);
    }

    //Logout
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, String email) {
        cookieUtil.deleteCookie(request, response, "REFRESH_TOKEN");
        refreshTokenRepository.deleteByEmail(email).orElseThrow(
                ()-> new NoSuchElementException("You are not the logged in user."));
        return new ResponseEntity<>("Successful logout.", HttpStatus.OK);
    }

    //Update Profile
    public ResponseEntity<?> updateProfile(MultipartFile imageFile, UpdateRequestDto requestDto, String email) {
        User user = findUser(email);
        checkDuplicatedNick(requestDto.getNickname());
        if(imageFile != null){
            awsS3Uploader.delete(user.getProfileImage());
            String storedFileUrl  = awsS3Uploader.uploadFile(imageFile);
            user.updateProfileImage(storedFileUrl);
        }
        user.updateProfile(requestDto);
        return new ResponseEntity<>("Edit your membership information", HttpStatus.OK);
    }

    //Reissue Token
    public ResponseEntity<?> reissueToken(String refreshToken, HttpServletResponse response) {
        jwtUtil.refreshTokenValid(refreshToken);
        String userEmail = jwtUtil.getUserInfoFromToken(refreshToken);
        User user = userRepository.findByEmail(userEmail).get();
        String newAccessToken = jwtUtil.createToken(user, "Access");
        response.setHeader("ACCESS_TOKEN", newAccessToken);
        return new ResponseEntity<>("Token reissued successfully!", HttpStatus.OK);
    }

    //Check for email duplicates
    public ResponseEntity<?> isDuplicatedEmail(CheckEmailRequestDto requestDto) {
        checkDuplicatedEmail(requestDto.getEmail());
        Map<String, Boolean> result = new HashMap<>();
        result.put("isDuplicatedEmail", false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Check for nickname duplicates
    public ResponseEntity<?> isDuplicatedNick(CheckNickRequestDto requestDto) {
        checkDuplicatedNick(requestDto.getNickname());
        Map<String, Boolean> result = new HashMap<>();
        result.put("isDuplicatedNick", false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public User findUser(String email){
        return userRepository.findByEmail(email).orElseThrow(()->
                new NoSuchElementException("The user does not exist."));
    }


    public void checkDuplicatedEmail(String email){
        Optional<User> findUserByEmail = userRepository.findByEmail(email);
        if (findUserByEmail.isPresent()) {
                throw new IllegalArgumentException("Using duplicate emails");
        }
    }

    public void checkDuplicatedNick(String nickname){
        Optional<User> findUserByNickname = userRepository.findByNickname(nickname);
        if (findUserByNickname.isPresent()) {
            throw new IllegalArgumentException("Using duplicate nicknames");
        }
    }

    public List<User> loadUserListByIdList(List<Long> userIdList){    // club멤버조회 시 사용
        return userRepository.findAllById(userIdList);
    }
    public User loadUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("User not found."));
    }
}
