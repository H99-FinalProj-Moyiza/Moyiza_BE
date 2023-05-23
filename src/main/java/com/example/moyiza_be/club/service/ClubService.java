package com.example.moyiza_be.club.service;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClubService {
    public ResponseEntity<Message> joinClub(Long clubId, UserDetailsImpl userDetails) {
        return null;
    }
}
