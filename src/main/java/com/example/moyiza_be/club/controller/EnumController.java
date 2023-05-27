package com.example.moyiza_be.club.controller;

import com.example.moyiza_be.club.dto.EnumOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enums")
public class EnumController {
    @GetMapping
    public ResponseEntity<EnumOptions> getEnumOptions(){
        return ResponseEntity.ok(new EnumOptions());
    }
}
