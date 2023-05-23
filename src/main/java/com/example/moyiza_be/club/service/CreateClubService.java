package com.example.moyiza_be.club.service;

import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.club.repository.CreateClubRepository;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateClubService {
    private final CreateClubRepository createClubRepository;
    public ResponseEntity<CreateClubIdResponse> initCreateClubId(User user) {
        CreateClub createClub = new CreateClub();
        createClubRepository.saveAndFlush(createClub);
        CreateClubIdResponse createClubIdResponse = new CreateClubIdResponse(createClub.getId());
        return ResponseEntity.ok(createClubIdResponse);
    }

    public ResponseEntity<Message> setCategory(User user, CategoryEnum categoryEnum) {
        return null;
    }

    public ResponseEntity<Message> setTag(User user, List<TagEnum> tagEnumList) {
        return null;
    }

    public ResponseEntity<Message> setTitle(User user, String title) {
        return null;
    }

    public ResponseEntity<Message> setContent(User user, String content) {
        return null;
    }

    public ResponseEntity<Message> setPolicy(
            User user, Calendar agePolicy, GenderPolicyEnum genderPolicyEnum) {
        return null;
    }
}
