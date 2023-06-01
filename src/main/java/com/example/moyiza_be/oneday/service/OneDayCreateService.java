package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.CreateOneDayIdResponseDto;
import com.example.moyiza_be.oneday.dto.CreatingDto;
import com.example.moyiza_be.oneday.entity.OneDayCreate;
import com.example.moyiza_be.oneday.repository.OneDayAttendantRepository;
import com.example.moyiza_be.oneday.repository.OneDayCreateRepository;
import com.example.moyiza_be.oneday.repository.OneDayImageUrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OneDayCreateService {
    private final OneDayService oneDayService;
    private final AwsS3Uploader s3Uploader;
    private final OneDayImageUrlRepository imageUrlRepository;
    private final OneDayCreateRepository createRepository;

    public ResponseEntity<?> initCreateOneDay(Long userId) {
        OneDayCreate bluePrint = createRepository.findByOwnerIdAndConfirmedIsFalse(userId).orElseThrow(()-> new IllegalArgumentException("작성중인 이벤트가 없습니다."));
        if (bluePrint != null) {
            // Message대신 작성중인 클럽 가져오기
            return new ResponseEntity<>(new Message("작성중인 원데이 가져오기"), HttpStatus.OK);
        }
        OneDayCreate oneDayCreate = new OneDayCreate();
        oneDayCreate.setOwnerId(userId);
        createRepository.save(oneDayCreate);
        CreateOneDayIdResponseDto createOneDayIdResponse = new CreateOneDayIdResponseDto(oneDayCreate.getId());
        return new ResponseEntity<>(createOneDayIdResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<CreatingDto> getExistCreatingOneDay(Long userId, Long createOneDayId) {

    }
}
