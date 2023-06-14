package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.OneDayDetailResponse;
import com.example.moyiza_be.oneday.dto.OneDayIdResponseDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneDayCreateService {
    private final OneDayService oneDayService;
    private final AwsS3Uploader s3Uploader;
    private final OneDayImageUrlRepository imageUrlRepository;
    private final OneDayCreateRepository createRepository;
    private final UserRepository userRepository;

    private final static LocalDateTime timeNow = LocalDateTime.now();
    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";

    // Temporary OneDay Create
    @Transactional
    public ResponseEntity<?> initCreateOneDay(Long userId) {
        OneDayCreate bluePrint = createRepository.findByOwnerIdAndConfirmedIsFalse(userId).orElse(null);
        if (bluePrint != null) {
            log.info("returning preexisting createoneday Id : " + bluePrint.getId());
            return new ResponseEntity<>(new OneDayIdResponseDto(bluePrint.getId()), HttpStatus.ACCEPTED);
        }
        log.info("has no previous createOneday");
        OneDayCreate oneDayCreate = new OneDayCreate(userId);
        createRepository.save(oneDayCreate);
        CreateOneDayIdResponseDto createOneDayIdResponse = new CreateOneDayIdResponseDto(oneDayCreate.getId());
        return new ResponseEntity<>(createOneDayIdResponse, HttpStatus.CREATED);
    }

    // load creating OneDay
    public ResponseEntity<CreatingDto> getExistCreatingOneDay(Long userId, Long createOneDayId) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        log.info("returning info for createoneday id : " + createOneDayId);
        return new ResponseEntity<>(new CreatingDto(oneDayCreate), HttpStatus.OK);
    }

    // title
    @Transactional
    public ResponseEntity<Message> setTitle(Long userId, Long createOneDayId, RequestTitleDto titleDto) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if(titleDto.getOneDayTitle() == null) {
            throw new NullPointerException("Please write down the Title.");
        }
        oneDayCreate.setOneDayTitle(titleDto.getOneDayTitle());
        log.info("set oneday title : " + titleDto.getOneDayTitle() +  "for id : " + createOneDayId);
        return new ResponseEntity<>(new Message("Success"), HttpStatus.OK);
    }
    // Content
    @Transactional
    public ResponseEntity<Message> setContent(Long userId, Long createOneDayId, RequestContentDto contentDto) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if(contentDto.getOneDayContent() == null) {
            throw new NullPointerException("Please write down the Content.");
        }
        oneDayCreate.setOneDayContent(contentDto.getOneDayContent());
        log.info("set oneday content : " + contentDto.getOneDayContent() +  "for id : " + createOneDayId);
        return new ResponseEntity<>(new Message("Success"), HttpStatus.OK);
    }

    // category
    @Transactional
    public ResponseEntity<Message> setCategory(Long userId, Long createOneDayId, RequestCategoryDto categoryEnum) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if(categoryEnum.getCategoryEnum() == null) {
            throw new NullPointerException("Please choose the Category.");
        }
        oneDayCreate.setCategory(categoryEnum.getCategoryEnum());
        log.info("set oneday category : " + categoryEnum.getCategoryEnum() +  "for id : " + createOneDayId);
        return new ResponseEntity<>(new Message("Success"), HttpStatus.OK);
    }

    // tag
    @Transactional
    public ResponseEntity<?> setTag(Long userId, Long createOneDayId, RequestTagDto tagEnumList) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if(tagEnumList.getTagEnumList() == null) {
            throw new NullPointerException("Please choose the Tag.");
        }
        String newStr = "0".repeat(TagEnum.values().length);
        StringBuilder sb = new StringBuilder(newStr);
        for(TagEnum tagEnum : tagEnumList.getTagEnumList()) {
            sb.setCharAt(tagEnum.ordinal(), '1');
        }
        oneDayCreate.setTagString(sb.toString());
        log.info("set oneday tag : " + sb.toString() +  "for id : " + createOneDayId);
        return new ResponseEntity<>(new Message("Success"),HttpStatus.OK);
    }

    // Policy
    @Transactional
    public ResponseEntity<Message> setPolicy
    (Long userId, Long createOneDayId, RequestPolicyDto policyRequest) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if(policyRequest.getGenderPolicy() == null) {
            throw new NullPointerException("Please choose the Gender.");
        } else if (policyRequest.getAgePolicy()==null) {
            throw new NullPointerException("Please choose the Age");
        }
        oneDayCreate.setGenderPolicy(GenderPolicyEnum.fromString(policyRequest.getGenderPolicy()));
        oneDayCreate.setAgePolicy(policyRequest.getAgePolicy());
        log.info("set oneday genderpolicy : " + policyRequest.getGenderPolicy() +  "for id : " + createOneDayId);
        log.info("set oneday agepolicy : " + policyRequest.getAgePolicy() +  "for id : " + createOneDayId);
        return new ResponseEntity<>(new Message("Success"),HttpStatus.OK);
    }

    // Size
    @Transactional
    public ResponseEntity<Message> setMaxGroupSize(Long userId, Long createOneDayId, RequestSizeDto maxSize) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if(maxSize.getSize()==null){
            throw new NullPointerException("Please write down the Size");
        }
        log.info("set oneday maxSize : " + maxSize.getSize() +  "for id : " + createOneDayId);
        oneDayCreate.setOneDayGroupSize(maxSize.getSize());
        log.info("set oneday oneDayGroupSize : " + oneDayCreate.getOneDayGroupSize() +  "for id : " + createOneDayId);
        return new ResponseEntity<>(new Message("Success"),HttpStatus.OK);
    }

    // Location
    @Transactional
    public ResponseEntity<Message> setLocation(Long userId, Long createOneDayId, RequestLocationDto requestLocationDto) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if (requestLocationDto.getOneDayLocation() == null || Objects.equals(requestLocationDto.getOneDayLongitude(),null) || Objects.equals(requestLocationDto.getOneDayLatitude(),null)){
            throw new NullPointerException("Please Fill up the location");
        }
        oneDayCreate.setOneDayLocation(requestLocationDto.getOneDayLocation());
        oneDayCreate.setOneDayLatitude(requestLocationDto.getOneDayLatitude());
        oneDayCreate.setOneDayLongitude(requestLocationDto.getOneDayLongitude());
        return new ResponseEntity<>(new Message("Success"),HttpStatus.OK);
    }

    // Date
    @Transactional
    public ResponseEntity<Message> setDate(Long userId, Long createOneDayId, RequestDateDto dateTime) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        if (dateTime.getOneDayStartTime() == null) {
            throw new RuntimeException("Enter The Start Time");
        } else if (timeNow.isAfter(dateTime.getOneDayStartTime())) {
            throw new RuntimeException("Invalid Date And Time");
        } else {
            oneDayCreate.setOneDayStartTime(dateTime.getOneDayStartTime());
        }
        return new ResponseEntity<>(new Message("Success"),HttpStatus.OK);
    }

    // revisit
    // Image
    @Transactional
    public ResponseEntity<Message> setImageList(Long userId, Long createOneDayId, MultipartFile imageFile) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        String imageUrl = null;
        log.info("Image List Setting");
        if (oneDayCreate.getOneDayImage().isEmpty()) {
            if (imageUrl == null) {
                log.info("Image File is Null, Set Image to Default File");
                imageUrl = DEFAULT_IMAGE_URL;
            }
        } else {
            log.info("Get Image File Complete");
            imageUrl = s3Uploader.uploadFile(imageFile);
        }
        log.info("image list setting complete");
//        List<OneDayImageUrl> imageEntityList = imageUrlList.stream().map(i -> new OneDayImageUrl(createOneDayId, i)).toList();
        OneDayImageUrl oneDayImageUrl = new OneDayImageUrl(createOneDayId, imageUrl);
        List<OneDayImageUrl> imageEntityList = new ArrayList<>();
        imageEntityList.add(oneDayImageUrl);
//        imageEntityList;
        imageUrlRepository.saveAll(imageEntityList);
        log.info("set thumbnail image");
        oneDayCreate.setOneDayImage(imageEntityList.get(0).getImageUrl());
        log.info("set : " + imageEntityList.size() +  "images for id : " + createOneDayId);

        return new ResponseEntity<>(new Message("Upload Complete!"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> setType(Long userId, Long createOneDayId, RequestTypeDto type) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setOneDayType(type.getOneDayTypeEnum());
        return new ResponseEntity<>(new Message("Success"),HttpStatus.OK);
    }

    // oneDayService.createOneDay is transactional, so we don't need it here
    public ResponseEntity<?> confirmCreation(User user, Long createOneDayId) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, user.getId());
        OneDayCreateConfirmDto confirmDto = new OneDayCreateConfirmDto(oneDayCreate);
        OneDayCreateService.nullCheck(oneDayCreate);
        OneDayDetailResponse newOneDay = oneDayService.createOneDay(user, confirmDto);
        oneDayCreate.setConfirmed(true);
        return new ResponseEntity<>(newOneDay, HttpStatus.OK);
    }
    ///////////////////////////////////

    private OneDayCreate loadOnedayCreate(Long createOnedayId, Long userId){
        return createRepository.findByIdAndOwnerIdAndConfirmedIsFalse(createOnedayId, userId)
                .orElseThrow(()->new NullPointerException("OnedayCreate not found"));
    }

    private static void nullCheck(OneDayCreate oneDayCreate) {
        if(oneDayCreate.getOneDayTitle() == null) {
            throw new NullPointerException("Title Is Empty");
        } else if (oneDayCreate.getOneDayContent()==null) {
            throw new NullPointerException("Content Is Empty");
        } else if (oneDayCreate.getCategory() == null) {
            throw new NullPointerException("Category Is Empty");
        } else if (oneDayCreate.getTagString() == null) {
            throw new NullPointerException("Tag Is Empty");
        } else if (oneDayCreate.getOneDayLocation()==null) {
            throw new NullPointerException("Location Is Empty");
        } else if (oneDayCreate.getOneDayGroupSize() == null) {
            throw new NullPointerException("GroupSize Is Empty");
        } else if (oneDayCreate.getOneDayStartTime()==null) {
            throw new NullPointerException("StartTime Is Empty");
        } else if (oneDayCreate.getOneDayImage()==null) {
            throw new NullPointerException("Image Is Empty");
        } else if (oneDayCreate.getOneDayType()!= OneDayTypeEnum.FCFSB && oneDayCreate.getOneDayType()!=OneDayTypeEnum.APPROVAL) {
            oneDayCreate.setOneDayType(OneDayTypeEnum.FCFSB);
        }
    }

}
