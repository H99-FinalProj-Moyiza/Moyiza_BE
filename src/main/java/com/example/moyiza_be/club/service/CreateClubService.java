package com.example.moyiza_be.club.service;

import com.example.moyiza_be.club.dto.ClubResponseDto;
import com.example.moyiza_be.club.dto.ConfirmClubCreationDto;
import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.club.repository.CreateClubRepository;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateClubService {
    private final CreateClubRepository createClubRepository;
    private final ClubService clubService;
    public ResponseEntity<?> initCreateClubId(Long userId) {
        if(createClubRepository.existsByOwnerId(userId)){
            return new ResponseEntity<>(new Message("이어서 작성하시겠습니까 ?"), HttpStatus.CONTINUE);
        }
        CreateClub createClub = new CreateClub();
        createClubRepository.saveAndFlush(createClub);
        CreateClubIdResponse createClubIdResponse = new CreateClubIdResponse(createClub.getId());
        return new ResponseEntity<>(createClubIdResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<CreateClubIdResponse> getPreviousCreateClub(Long userId, Long createclub_id) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        return ResponseEntity.ok(new CreateClubIdResponse(createclub_id));
    }

    public ResponseEntity<Message> setCategory(User user, Long createclub_id, CategoryEnum categoryEnum) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        createClub.setCategory(categoryEnum);
        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setTag(User user, Long createclub_id, List<TagEnum> tagEnumList) {
        CreateClub createClub = loadCreateClubById(createclub_id);

        String newString = "0".repeat(TagEnum.values().length);

        StringBuilder sb = new StringBuilder(newString);
        for(TagEnum tagEnum : tagEnumList){
            sb.setCharAt(tagEnum.ordinal(), '1');
        }
        createClub.setTagString(sb.toString());

        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setTitle(User user, Long createclub_id, String title) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        createClub.setTitle(title);
        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setContent(User user, Long createclub_id, String content) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        createClub.setContent(content);
        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<Message> setPolicy(
            User user, Long createclub_id, Calendar agePolicy, GenderPolicyEnum genderPolicyEnum) {
        CreateClub createclub = loadCreateClubById(createclub_id);
        createclub.setGenderPolicy(genderPolicyEnum);
        createclub.setAgePolicy(agePolicy);   // 모두 가능할시 null로 받아서 null로 세팅 가능한지 알아봐야함

        return ResponseEntity.ok(new Message("성공"));
    }

    public ResponseEntity<ClubResponseDto> confirmCreation(UserDetailsImpl userDetails, Long createclub_id) {
        CreateClub createClub = loadCreateClubById(createclub_id);
        ConfirmClubCreationDto confirmClubCreationDto = new ConfirmClubCreationDto(createClub);
        ClubResponseDto newClub = clubService.createClub(confirmClubCreationDto);
        return ResponseEntity.ok(newClub);

    }

    ///////////////////////private methods///////////////////

    private CreateClub loadCreateClubById(Long createclub_id) {
        return createClubRepository.findById(createclub_id)
                .orElseThrow(() -> new NullPointerException("생성중인 클럽을 찾을 수 없습니다"));
    }
}
