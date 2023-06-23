package com.example.moyiza_be.user.service;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.common.enums.BasicProfileEnum;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.redis.RedisUtil;
import com.example.moyiza_be.common.security.jwt.CookieUtil;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.jwt.refreshToken.RefreshTokenRepository;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.BadWordFiltering;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.repository.OneDayRepository;
import com.example.moyiza_be.user.dto.*;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import com.example.moyiza_be.user.sms.SmsUtil;
import com.example.moyiza_be.user.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final SmsUtil smsUtil;
    private final ValidationUtil validationUtil;
    private final RedisUtil redisUtil;
    private final BadWordFiltering badWordFiltering;
    private final ClubRepository clubRepository;
    private final OneDayRepository oneDayRepository;

    //Signup
    public ResponseEntity<?> signup(SignupRequestDto testRequestDto) {
        String password = passwordEncoder.encode(testRequestDto.getPassword());
        validationUtil.checkDuplicatedEmail(testRequestDto.getEmail());
        validationUtil.checkDuplicatedNick(testRequestDto.getNickname());
        User user = new User(password, testRequestDto);
        user.authorizeUser();
        userRepository.save(user);
        return new ResponseEntity<>("Signup complete!", HttpStatus.OK);
    }

    public ResponseEntity<?> updateSocialInfo(UpdateSocialInfoRequestDto requestDto, User user) {
        User foundUser = validationUtil.findUserBySocialInfo(user.getSocialType(), user.getSocialLoginId());
        validationUtil.checkDuplicatedNick(requestDto.getNickname());
        foundUser.updateSocialInfo(requestDto);
        foundUser.authorizeUser();
        return new ResponseEntity<>("Social signup complete!", HttpStatus.OK);
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
        User user = validationUtil.findUserByEmail(email);
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
        return new ResponseEntity<>("Successful logout", HttpStatus.OK);
    }

    public ResponseEntity<?> tagsOfCategory(String category) {
        CategoryEnum categoryEnum = CategoryEnum.fromString(category);
        TagResponseDto responseDto = new TagResponseDto(TagEnum.tagEnumListOfCategory(categoryEnum));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //Reissue Token
    public ResponseEntity<?> reissueToken(String refreshToken, HttpServletResponse response) {
        jwtUtil.refreshTokenValid(refreshToken);
        String userEmail = jwtUtil.getUserInfoFromToken(refreshToken);
        User user = userRepository.findByEmail(userEmail).get();
        String newAccessToken = jwtUtil.createToken(user, "Access");
        response.setHeader("ACCESS_TOKEN", newAccessToken);
        return new ResponseEntity<>("Successful token reissue!", HttpStatus.OK);
    }

    //Check for email duplicates
    public ResponseEntity<?> isDuplicatedEmail(CheckEmailRequestDto requestDto) {
        validationUtil.checkDuplicatedEmail(requestDto.getEmail());
        Map<String, Boolean> result = new HashMap<>();
        result.put("isDuplicatedEmail", false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Check for nickname duplicates
    public ResponseEntity<?> isDuplicatedNick(CheckNickRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        validationUtil.checkDuplicatedNick(nickname);
        if(badWordFiltering.checkBadNick(nickname)){
            return new ResponseEntity<>(new Message("고운말을 씁시다."), HttpStatus.BAD_REQUEST);
        }
        Map<String, Boolean> result = new HashMap<>();
        result.put("isDuplicatedNick", false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Find email - Send text
    public ResponseEntity<?> sendSmsToFindEmail(FindEmailRequestDto requestDto) {
        String name = requestDto.getName();
        String phoneNum = requestDto.getPhone().replaceAll("-","");
        User foundUser = userRepository.findByNameAndPhone(name, phoneNum).orElseThrow(()->
                new NoSuchElementException("The user does not exist."));
        String receiverEmail = foundUser.getEmail();
        String verificationCode = validationUtil.createCode();
        smsUtil.sendSms(phoneNum, verificationCode);
        redisUtil.setDataExpire(verificationCode, receiverEmail, 60 * 5L); //Valid for 5 minutes

        return new ResponseEntity<>("Text sent successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> verifyCodeToFindEmail(String code) {
        String userEmail = redisUtil.getData(code);
        if(userEmail == null){
            throw new NullPointerException("Invalid authentication number.");
        }
        redisUtil.deleteData(code);
        EmailResponseDto responseDto = new EmailResponseDto(userEmail);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //Test
    public ResponseEntity<?> uploadImg(MultipartFile image) {
        if(image == null || image.isEmpty()){
            return new ResponseEntity<>(BasicProfileEnum.getRandomImage().getImageUrl(), HttpStatus.OK);
        }
        String storedFileUrl  = awsS3Uploader.uploadFile(image);
        return new ResponseEntity<>(storedFileUrl, HttpStatus.OK);
    }

    public ResponseEntity<?> updateProfile(UpdateRequestDto requestDto, String email) {
        User user = validationUtil.findUserByEmail(email);
        if (requestDto.getNickname() != null && !requestDto.getNickname().isEmpty()) {
            validationUtil.checkDuplicatedNick(requestDto.getNickname());
            user.setNickname(requestDto.getNickname());
        }
        if (requestDto.getTags() != null && !requestDto.getTags().isEmpty()) {
            List<TagEnum> tagEnumList = requestDto.getTagEnumList();
            String newString = "0".repeat(TagEnum.values().length);
            StringBuilder tagBuilder = new StringBuilder(newString);
            for (TagEnum tagEnum : tagEnumList) {
                tagBuilder.setCharAt(tagEnum.ordinal(), '1');
            }
            user.setTagString(tagBuilder.toString());
        }
        if (requestDto.getImageUrl() != null && !requestDto.getImageUrl().isEmpty()) {
            user.setProfileImage(requestDto.getImageUrl());
        }
        if (requestDto.getContent() != null && !requestDto.getContent().isEmpty()) {
            user.setContent(requestDto.getContent());
        }
        return new ResponseEntity<>("Edit your membership information", HttpStatus.OK);
    }

    public List<User> loadUserListByIdList(List<Long> userIdList){    // Used to view club members
        return userRepository.findAllById(userIdList);
    }

    public User loadUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("User not found"));
    }

    public ResponseEntity<?> withdrawUser(User user) {
        Long userId = user.getId();
        User withdrawUser = loadUserById(userId);
        withdrawUser.setIsDeleted(true);
        List<Club> withdrawUsersClubs = clubRepository.findByOwnerId(userId);
        for (Club club : withdrawUsersClubs) {
            club.flagDeleted(true);
        }
        List<OneDay> withdrawUsersOneDays = oneDayRepository.findAllByOwnerId(userId);
        for (OneDay oneday : withdrawUsersOneDays) {
            oneday.setDeleted(true);
        }
        return new ResponseEntity<>(new Message("User withdraw success"), HttpStatus.OK);
    }

    //Modify Profile - test
//    public ResponseEntity<?> updateProfile(MultipartFile imageFile, UpdateRequestDto requestDto, String email) {
//        User user = validationUtil.findUser(email);
//        validationUtil.checkDuplicatedNick(requestDto.getNickname());
//
//        if(imageFile != null){
//            awsS3Uploader.delete(user.getProfileImage());
//            String storedFileUrl  = awsS3Uploader.uploadFile(imageFile);
//            user.updateProfileImage(storedFileUrl);
//        }
//
//        List<TagEnum> tagEnumList = requestDto.getTagEnumList();
//        String newString = "0".repeat(TagEnum.values().length);
//        StringBuilder tagBuilder = new StringBuilder(newString);
//        for (TagEnum tagEnum : tagEnumList) {
//            tagBuilder.setCharAt(tagEnum.ordinal(), '1');
//        }
//        user.updateProfile(requestDto.getNickname(), tagBuilder.toString());
//
//        return new ResponseEntity<>("Edit your membership information", HttpStatus.OK);
//    }
}
