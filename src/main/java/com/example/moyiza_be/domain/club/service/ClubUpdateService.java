package com.example.moyiza_be.domain.club.service;


import com.example.moyiza_be.domain.club.dto.AgePolicyUpdateRequest;
import com.example.moyiza_be.domain.club.dto.ClubRuleUpdateRequest;
import com.example.moyiza_be.domain.club.dto.GenderPolicyUpdateRequest;
import com.example.moyiza_be.domain.club.dto.RemoveImageRequest;
import com.example.moyiza_be.domain.club.dto.createclub.*;
import com.example.moyiza_be.domain.club.dto.createclub.*;
import com.example.moyiza_be.domain.club.entity.Club;
import com.example.moyiza_be.domain.club.entity.ClubImageUrl;
import com.example.moyiza_be.domain.club.repository.ClubImageUrlRepository;
import com.example.moyiza_be.domain.club.repository.ClubRepository;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubUpdateService {
    private final ClubRepository clubRepository;
    private final AwsS3Uploader s3Utils;
    private final ClubImageUrlRepository clubImageUrlRepository;

    @Transactional
    public ResponseEntity<Message> updateClubTitle(User user, Long clubId, CreateRequestTitleDto titleDto) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setTitle(titleDto.getTitle());
        log.info("clubTitle updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("title update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubCategory(User user, Long clubId, CreateRequestCategoryDto categoryDto) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setCategory(categoryDto.getCategoryEnum());
        log.info("clubCategory updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("category update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubTag(User user, Long clubId, CreateRequestTagDto tagDto) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        String tagString = TagEnum.tagListToTagString(tagDto.getTag());
        club.setTagString(tagString);
        log.info("clubTag updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("tag update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubContent(User user, Long clubId, CreateRequestContentDto contentDto) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setContent(contentDto.getContent());
        log.info("clubContent updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("content update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubAgePolicy(User user, Long clubId, AgePolicyUpdateRequest agePolicy) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setAgePolicy(agePolicy.getAgePolicy());
        log.info("clubAgePolicy updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("agePolicy update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubGenderPolicy(User user, Long clubId, GenderPolicyUpdateRequest genderPolicy) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setGenderPolicy(GenderPolicyEnum.fromString(genderPolicy.getGenderPolicy()));
        log.info("clubGenderPolicy updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("genderPolicy update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubMaxGroupSize(User user, Long clubId, CreateRequestMaxSizeDto maxSizeDto) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setMaxGroupSize(maxSizeDto.getMaxGroupSize());
        log.info("clubMaxGroupSize updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("maxGroupSize update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubImage(User user,Long clubId, List<MultipartFile> imageFileList, RemoveImageRequest removeImageRequest) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        if(removeImageRequest != null){
            log.info("removing " + removeImageRequest.getDeleteImage().size() + " images ...");
            for(String url:removeImageRequest.getDeleteImage()){
                s3Utils.delete(url);
                clubImageUrlRepository.deleteByImageUrl(url);
            }
            log.info("image delete successful");
        }
        List<String> imageUrlList;
        if (imageFileList != null) {
            log.info("uploading " + imageFileList.size() + " files to S3 imageBucket");
            imageUrlList = s3Utils.uploadMultipleImg(imageFileList);
            List<ClubImageUrl> imageEntityList = imageUrlList.stream().map(i -> new ClubImageUrl(-1L, clubId, i)).toList();
            clubImageUrlRepository.saveAll(imageEntityList);
            log.info("upload successful");
        }

        return ResponseEntity.ok(new Message("clubImage update successful"));
    }

    @Transactional
    public ResponseEntity<Message> updateClubRule(
            User user, Long clubId, ClubRuleUpdateRequest clubRuleUpdateRequest
    ) {
        Club club = loadClubAndCheckOwnership(user.getId(), clubId);
        club.setClubRule(clubRuleUpdateRequest.getClubRule());
        log.info("clubRule updated for clubId : " + clubId);
        return ResponseEntity.ok(new Message("clubRule update successful"));
    }


    private Club loadClubAndCheckOwnership(Long userId, Long clubId){
        Club club = clubRepository.findById(clubId).orElseThrow(()-> new NullPointerException("club not found"));
        if(!club.getOwnerId().equals(userId)){
            throw new IllegalArgumentException("not authorized for club update");
        }
        else{
            return club;
        }
    }
}

