package com.example.moyiza_be.club.service;

import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.club.repository.CreateClubRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
