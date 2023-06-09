package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
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
        OneDayCreate oneDayCreate = new OneDayCreate(userId);
        createRepository.save(oneDayCreate);
        CreateOneDayIdResponseDto createOneDayIdResponse = new CreateOneDayIdResponseDto(oneDayCreate.getId());
        return new ResponseEntity<>(createOneDayIdResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<CreatingDto> getExistCreatingOneDay(Long userId, Long createOneDayId) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        return new ResponseEntity<>(new CreatingDto(oneDayCreate), HttpStatus.OK);
    }

    public ResponseEntity<Message> setTitle(Long userId, Long createOneDayId, RequestTitleDto titleDto) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setOneDayTitle(titleDto.getOneDayTitle());
        return new ResponseEntity<>(new Message("성공"), HttpStatus.OK);
    }

    public ResponseEntity<Message> setContent(Long userId, Long createOneDayId, RequestContentDto contentDto) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setOneDayContent(contentDto.getOneDayContent());
        return new ResponseEntity<>(new Message("성공"), HttpStatus.OK);
    }
    public ResponseEntity<Message> setCategory(Long userId, Long createOneDayId, RequestCategoryDto categoryEnum) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setCategory(categoryEnum.getCategoryEnum());
        return new ResponseEntity<>(new Message("설정 완료"), HttpStatus.OK);
    }

    public ResponseEntity<?> setTag(Long userId, Long createOneDayId, RequestTagDto tagEnumList) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        String newStr = "0".repeat(TagEnum.values().length);
        StringBuilder sb = new StringBuilder(newStr);
        for(TagEnum tagEnum : tagEnumList.getTagEnumList()) {
            sb.setCharAt(tagEnum.ordinal(), '1');
        }
        oneDayCreate.setTagString(sb.toString());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    ///////
    public ResponseEntity<Message> setPolicy
            (Long userId, Long createOneDayId, RequestPolicyDto policyRequest) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setGenderPolicy(GenderPolicyEnum.fromString(policyRequest.getGenderPolicy()));
        oneDayCreate.setAgePolicy(policyRequest.getAgePolicy());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }
    public ResponseEntity<Message> setMaxGroupSize(Long userId, Long createOneDayId, RequestSizeDto maxSize) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        System.out.println(maxSize.getSize());
        oneDayCreate.setOneDayGroupSize(maxSize.getSize());
        System.out.println("여기야");
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    // 위치
    public ResponseEntity<Message> setLocation(Long userId, Long createOneDayId, RequestLocationDto requestLocationDto) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setOneDayLocation(requestLocationDto.getOneDayLocation());
        oneDayCreate.setOneDayLatitude(requestLocationDto.getOneDayLatitude());
        oneDayCreate.setOneDayLongitude(requestLocationDto.getOneDayLongitude());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    // 날짜
    public ResponseEntity<Message> setDate(Long userId, Long createOneDayId, RequestDateDto dateTime) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setOneDayStartTime(dateTime.getOneDayStartTime());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }


    //revisit
    public ResponseEntity<Message> setImageList(Long userId, Long createOneDayId, List<MultipartFile> imageFileList) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
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

    public ResponseEntity<Message> setType(Long userId, Long createOneDayId, RequestTypeDto type) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, userId);
        oneDayCreate.setOneDayType(type.getOneDayTypeEnum());
        return new ResponseEntity<>(new Message("성공"),HttpStatus.OK);
    }

    public ResponseEntity<?> confirmCreation(User user, Long createOneDayId) {
        OneDayCreate oneDayCreate = loadOnedayCreate(createOneDayId, user.getId());
        OneDayCreateConfirmDto confirmDto = new OneDayCreateConfirmDto(oneDayCreate);
        OneDayDetailResponse newOneDay = oneDayService.createOneDay(user, confirmDto);
        oneDayCreate.setConfirmed(true);
        return new ResponseEntity<>(newOneDay, HttpStatus.OK);
    }
    ///////////////////////////////////

    private OneDayCreate loadOnedayCreate(Long createOnedayId, Long userId){
        return createRepository.findByIdAndOwnerIdAndConfirmedIsFalse(createOnedayId, userId)
                .orElseThrow(()->new NullPointerException("OnedayCreate not found"));
    }

}
