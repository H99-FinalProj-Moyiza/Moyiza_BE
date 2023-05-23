package com.example.moyiza_be.club.service;

import com.example.moyiza_be.club.dto.ClubResponseDto;
import com.example.moyiza_be.club.dto.ConfirmClubCreationDto;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClubService {
    public ResponseEntity<Message> joinClub(Long clubId, UserDetailsImpl userDetails) {
        return null;
    }

    @Transactional
    public ClubResponseDto createClub(ConfirmClubCreationDto creationRequest){



        //생성해주신 후, ClubResponseDto 반환해주시면 됩니다
        return null;
    }
}
