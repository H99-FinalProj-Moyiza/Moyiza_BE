package com.example.moyiza_be.club.service;

import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.club.dto.*;
import com.example.moyiza_be.club.entity.ClubImageUrl;
import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.club.repository.ClubImageUrlRepository;
import com.example.moyiza_be.club.repository.CreateClubRepository;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateClubService {
    private final CreateClubRepository createClubRepository;
    private final ClubService clubService;
    private final AwsS3Uploader imageUploader;
    private final ClubImageUrlRepository clubImageUrlRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final static Integer CLUB_OWNERSHIP_MAX_COUNT = 100;

    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";


    @Transactional
    public ResponseEntity<?> initCreateClubId(Long userId) {
        CreateClub previousCreate = createClubRepository.findByOwnerIdAndFlagConfirmedIsFalse(userId).orElse(null);
        if (previousCreate != null) {
            log.info("found existing club by user " + userId);
            return new ResponseEntity<>(new ResumeIdResponse(previousCreate.getId()), HttpStatus.ACCEPTED);
        }
        if (clubService.userOwnedClubCount(userId) >= CLUB_OWNERSHIP_MAX_COUNT) {
            log.info("createClub denied by user " + userId + " having too many club");
            throw new IllegalArgumentException("You can't have more than " + CLUB_OWNERSHIP_MAX_COUNT + " clubs");
        }
        CreateClub createClub = new CreateClub();
        createClub.setOwnerId(userId);
        createClubRepository.saveAndFlush(createClub);
        log.info("new club created : club ID " + createClub.getId());
        CreateClubIdResponse createClubIdResponse = new CreateClubIdResponse(createClub.getId());
        return new ResponseEntity<>(createClubIdResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<ResumeCreationDto> getPreviousCreateClub(Long userId, Long createclub_id) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        log.info("returning previous createclub : " + createclub_id);
        return ResponseEntity.ok(new ResumeCreationDto(createClub));
    }

    @Transactional
    public ResponseEntity<Message> setCategory(Long userId, Long createclub_id, CategoryEnum categoryEnum) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setCategory(categoryEnum);
        return ResponseEntity.ok(new Message("Success"));
    }

    @Transactional
    public ResponseEntity<Message> setTag(Long userId, Long createclub_id, List<String> tagList) {
        System.out.println("tagList.toString() = " + tagList.toString());
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        String newString = TagEnum.tagListToTagString(tagList);
        System.out.println("newString = " + newString);
        createClub.setTagString(newString);

        return ResponseEntity.ok(new Message("Success"));
    }

    @Transactional
    public ResponseEntity<Message> setTitle(Long userId, Long createclub_id, String title) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setTitle(title);
        return ResponseEntity.ok(new Message("Success"));
    }

    @Transactional
    public ResponseEntity<Message> setContent(Long userId, Long createclub_id, String content) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setContent(content);
        return ResponseEntity.ok(new Message("Success"));
    }

    @Transactional
    public ResponseEntity<Message> setPolicy
            (Long userId, Long createclub_id, Integer agePolicy, GenderPolicyEnum genderPolicyEnum) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setGenderPolicy(genderPolicyEnum);
        createClub.setAgePolicy(agePolicy);

        return ResponseEntity.ok(new Message("Success"));
    }

    @Transactional
    public ResponseEntity<Message> setMaxGroupSize(Long userId, Long createclub_id, Integer requestMaxSize) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setMaxGroupSize(requestMaxSize);
        return ResponseEntity.ok(new Message("Success"));
    }


    @Transactional
    public ResponseEntity<Message> setImageList(Long userId, Long createclub_id, List<MultipartFile> imageFileList) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        List<String> imageUrlList;
        if (imageFileList == null) {
            log.info("no input image : setting default image...");
            imageUrlList = List.of(DEFAULT_IMAGE_URL);
        } else {
            imageUrlList = imageUploader.uploadMultipleImg(imageFileList);
        }
        createClub.setThumbnailUrl(imageUrlList.get(0));
        List<ClubImageUrl> imageEntityList = imageUrlList.stream().map(i -> new ClubImageUrl(createclub_id, i)).toList();
        clubImageUrlRepository.saveAll(imageEntityList);

        return ResponseEntity.ok(new Message("Image upload complete!"));
    }

    // clubService.createClub is transactional, so we don't need it here
    public ResponseEntity<ClubDetailResponse> confirmCreation(User user, Long createclub_id) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, user.getId());
        ConfirmClubCreationDto confirmClubCreationDto = new ConfirmClubCreationDto(createClub);
        ClubDetailResponse newClub = clubService.createClub(confirmClubCreationDto, user);
        createClub.setFlagConfirmed(true);
        return ResponseEntity.ok(newClub);
    }
    //////////////////////private methods///////////////////

    private CreateClub loadCreateClubById(Long createclub_id) {
        return createClubRepository.findByIdAndFlagConfirmedFalse(createclub_id)
                .orElseThrow(() -> new NullPointerException("Can't find the club you're creating"));
    }

    private Boolean checkCreateClubOwnerShip(CreateClub createClub, Long userId) {
        return createClub.getOwnerId().equals(userId);
    }

    private CreateClub loadAndCheckOwnerShip(Long createclub_id, Long userId) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        if (!checkCreateClubOwnerShip(createClub, userId)) {
            log.info("loadCreateClub denied : user " + userId + " does not own createClub " + createclub_id);
            throw new IllegalCallerException("Not my createClub.");
        }
        return createClub;
    }

}
