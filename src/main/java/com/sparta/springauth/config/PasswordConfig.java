package com.sparta.springauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig { // passwordConfig가 Bean으로 등록됨

    @Bean
    public PasswordEncoder passwordEncoder() { // passwordEncoder가 Bean으로 등록됨
        return new BCryptPasswordEncoder();
    }
    //BCrypt : 비밀번호를 암호화해주는 Hash 함수이다.
}