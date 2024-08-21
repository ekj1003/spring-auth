package com.sparta.springauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private boolean admin = false;
    // false: 일반 권한
    // true: 관리자 권한
    private String adminToken = "";
}