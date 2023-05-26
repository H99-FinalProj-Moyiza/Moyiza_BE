package com.example.moyiza_be.club.service;

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
@Transactional
public class CreateClubService {
    private final CreateClubRepository createClubRepository;
    private final ClubService clubService;
    private final AwsS3Uploader imageUploader;
    private final ClubImageUrlRepository clubImageUrlRepository;
    private final static Integer CLUB_OWNERSHIP_MAX_COUNT = 100;

    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";


    public ResponseEntity<?> initCreateClubId(Long userId) {
        CreateClub previousCreate = createClubRepository.findByOwnerIdAndFlagConfirmedIsFalse(userId).orElse(null);
        if (previousCreate != null) {
            log.info("found existing club by user " + userId);
            return new ResponseEntity<>(new ResumeIdResponse(previousCreate.getId()), HttpStatus.ACCEPTED);
        }
        if (clubService.userOwnedClubCount(userId) >= CLUB_OWNERSHIP_MAX_COUNT) {
            log.info("createClub denied by user " + userId + " having 3 or more club");
            throw new IllegalArgumentException("클럽을 " + CLUB_OWNERSHIP_MAX_COUNT + " 개 이상 가질 수 없습니다");
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

    public ResponseEntity<Message> setCategory(Long userId, Long createclub_id, CategoryEnum categoryEnum) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setCategory(categoryEnum);
        return ResponseEntity.ok(new MessageWithTagOptionsDto("성공", categoryEnum));

    }

    public ResponseEntity<Message> setTag(Long userId, Long createclub_id, List<TagEnum> tagEnumList) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        String newString = "0".repeat(TagEnum.values().length);

        StringBuilder sb = new StringBuilder(newString);
        for (TagEnum tagEnum : tagEnumList) {
            sb.setCharAt(tagEnum.ordinal(), '1');
        }
        createClub.setTagString(sb.toString());

        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setTitle(Long userId, Long createclub_id, String title) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setTitle(title);
        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setContent(Long userId, Long createclub_id, String content) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setContent(content);
        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setPolicy
            (Long userId, Long createclub_id, Integer agePolicy, GenderPolicyEnum genderPolicyEnum) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setGenderPolicy(genderPolicyEnum);
        createClub.setAgePolicy(agePolicy);   // 모두 가능할시 null로 받아서 null로 세팅 가능한지 알아봐야함

        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setMaxGroupSize(Long userId, Long createclub_id, Integer requestMaxSize) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        createClub.setMaxGroupSize(requestMaxSize);
        return ResponseEntity.ok(new Message("성공"));
    }


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

        return ResponseEntity.ok(new Message("이미지 업로드 완료 !"));
    }

    public ResponseEntity<ClubDetailResponse> confirmCreation(Long userId, Long createclub_id) {
        CreateClub createClub = loadAndCheckOwnerShip(createclub_id, userId);
        ConfirmClubCreationDto confirmClubCreationDto = new ConfirmClubCreationDto(createClub);
        ClubDetailResponse newClub = clubService.createClub(confirmClubCreationDto);
        createClub.setFlagConfirmed(true);
        return ResponseEntity.ok(newClub);
    }

    //////////////////////private methods///////////////////

    private CreateClub loadCreateClubById(Long createclub_id) {
        return createClubRepository.findByIdAndFlagConfirmedFalse(createclub_id)
                .orElseThrow(() -> new NullPointerException("생성중인 클럽을 찾을 수 없습니다"));
    }

    private Boolean checkCreateClubOwnerShip(CreateClub createClub, Long userId) {
        return createClub.getOwnerId().equals(userId);
    }

    private CreateClub loadAndCheckOwnerShip(Long createclub_id, Long userId) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        if (!checkCreateClubOwnerShip(createClub, userId)) {
            log.info("loadCreateClub denied : user " + userId + " does not own createClub " + createclub_id);
            throw new IllegalCallerException("내 createClub이 아닙니다");
        }
        return createClub;
    }

}
