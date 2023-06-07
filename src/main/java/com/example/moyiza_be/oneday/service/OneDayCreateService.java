package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.club.dto.createclub.CreateRequestCategoryDto;
import com.example.moyiza_be.club.dto.createclub.CreateRequestTagDto;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.*;
import com.example.moyiza_be.oneday.dto.onedaycreate.*;
import com.example.moyiza_be.oneday.entity.OneDayCreate;
import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import com.example.moyiza_be.oneday.repository.OneDayCreateRepository;
import com.example.moyiza_be.oneday.repository.OneDayImageUrlRepository;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OneDayCreateService {
    private final OneDayService oneDayService;
    private final AwsS3Uploader s3Uploader;
    private final OneDayImageUrlRepository imageUrlRepository;
    private final OneDayCreateRepository createRepository;
    private final UserRepository userRepository;

    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";


    public ResponseEntity<?> initCreateOneDay(Long userId) {
        OneDayCreate bluePrint = createRepository.findByOwnerIdAndConfirmedIsFalse(userId).orElse(null);
        if (bluePrint != null) {
            return new ResponseEntity<>(new OneDayIdResponseDto(bluePrint.getId()), HttpStatus.ACCEPTED);
        }
        OneDayCreate oneDayCreate = new OneDayCreate();
        oneDayCreate.setOwnerId(userId);
        createRepository.save(oneDayCreate);
        CreateOneDayIdResponseDto createOneDayIdResponse = new CreateOneDayIdResponseDto(oneDayCreate.getId());
        return new ResponseEntity<>(createOneDayIdResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<CreatingDto> getExistCreatingOneDay(Long userId, Long createOneDayId) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(() -> new NullPointerException("생성중인 원데이가 없어요"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        if(oneDayCreate.getOneDayGroupSize()==null){
            oneDayCreate.setOneDayGroupSize(3);
        }
        return new ResponseEntity<>(new CreatingDto(oneDayCreate), HttpStatus.OK);
    }

    public ResponseEntity<Message> setTitle(Long userId, Long createOneDayId, RequestTitleDto titleDto) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setOneDayTitle(titleDto.getOneDayTitle());
        return new ResponseEntity<>(new Message("성공"), HttpStatus.OK);
    }

    public ResponseEntity<Message> setContent(Long userId, Long createOneDayId, RequestContentDto contentDto) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setOneDayContent(contentDto.getOneDayContent());
        return new ResponseEntity<>(new Message("성공"), HttpStatus.OK);
    }
    public ResponseEntity<Message> setCategory(Long userId, Long createOneDayId, CreateRequestCategoryDto categoryEnum) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("생성중인 원데이가 없어요"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setCategory(categoryEnum.getCategoryEnum());
        return new ResponseEntity<>(new Message("설정 완료"), HttpStatus.OK);
    }

    public ResponseEntity<?> setTag(Long userId, Long createOneDayId, CreateRequestTagDto tagEnumList) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("생성중인 원데이가 없어요"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        String newStr = "0".repeat(TagEnum.values().length);
        StringBuilder sb = new StringBuilder(newStr);
        for(TagEnum tagEnum : tagEnumList.getTagEnumList()) {
            sb.setCharAt(tagEnum.ordinal(), '1');
        }
        oneDayCreate.setTagString(sb.toString());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }
    public ResponseEntity<Message> setPolicy
            (Long userId, Long createOneDayId, RequestPolicyDto policy) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        // loadAndCheckOwnerShip
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setGenderPolicy(policy.getGenderPolicy());
        oneDayCreate.setAgePolicy(policy.getAgePolicy());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }
    public ResponseEntity<Message> setMaxGroupSize(Long userId, Long createOneDayId, RequestSizeDto maxSize) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setOneDayGroupSize(maxSize.getSize());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    // 위치
    public ResponseEntity<Message> setLocation(Long userId, Long createOneDayId, RequestLocationDto requestLocationDto) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setOneDayLocation(requestLocationDto.getOneDayLocation());
        oneDayCreate.setOneDayLatitude(requestLocationDto.getOneDayLatitude());
        oneDayCreate.setOneDayLongitude(requestLocationDto.getOneDayLongitude());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    // 날짜
    public ResponseEntity<Message> setDate(Long userId, Long createOneDayId, RequestDateDto dateTime) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        if(dateTime.getDateTime()==null){
            dateTime.setDateTime(LocalDateTime.now());
        }
        oneDayCreate.setOneDayStartTime(dateTime.getDateTime());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    public ResponseEntity<Message> setImageList(Long userId, Long createOneDayId, List<MultipartFile> imageFileList) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        List<String> imageUrlList;
        if(imageFileList == null) {
            imageUrlList = List.of(DEFAULT_IMAGE_URL);
        } else {
            imageUrlList = s3Uploader.uploadMultipleImg(imageFileList);
        }
        oneDayCreate.setOneDayImage(imageUrlList.get(0));
        List<OneDayImageUrl> imageEntityList = imageUrlList.stream().map(i -> new OneDayImageUrl(createOneDayId, i)).toList();
        imageUrlRepository.saveAll(imageEntityList);

        return new ResponseEntity<>(new Message("이미지 업로드 완료 !"), HttpStatus.OK);
    }

    public ResponseEntity<?> confirmCreation(Long userId, Long createOneDayId) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("login First"));
        OneDayCreateConfirmDto confirmDto = new OneDayCreateConfirmDto(oneDayCreate);
        OneDayDetailResponse newOneDay = oneDayService.createOneDay(confirmDto);
        oneDayCreate.setConfirmed(true);
        return new ResponseEntity<>(newOneDay, HttpStatus.OK);
    }

    public ResponseEntity<Message> setType(Long userId, Long createOneDayId, RequestTypeDto type) {
        OneDayCreate oneDayCreate = createRepository.findByIdAndConfirmedIsFalse(createOneDayId).orElseThrow(()->new NullPointerException("No Such OneDay Found"));
        if(!oneDayCreate.getOwnerId().equals(userId)) {
            throw new IllegalCallerException("It is not your oneday");
        }
        oneDayCreate.setOneDayType(type.getTypeEnum());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }
}
