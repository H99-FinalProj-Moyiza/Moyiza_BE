package com.example.moyiza_be.user.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Integer gender;  // 0 : 여성,  1 : 남성
    private Calendar birth;
    private String profileUrl;
}