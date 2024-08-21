package com.sparta.springauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("수동 등록한 passwordEncoder를 주입 받아와 문자열 암호화")
    void test1() {
        String password = "Robbie's password";

        // 암호화
        String encodePassword = passwordEncoder.encode(password);
        System.out.println("encodePassword = " + encodePassword);

        String inputPassword = "Robbie";

        // 복호화를 통해 암호화된 비밀번호와 비교
        /**
         * 그냥 사용자가 친 password와 암호화된 password가 일치한지를 보는 건데
         * matches가 내부에서 inputPassword를 암호화하여 encodePassword와 같은 지 비교함.
         * param1: 입력 받은 password
         * param2: 암호화한 password
         * return은 false 일 것
         * Why? encodePassword는 "Robbie's password"을 암호화한 것이고, inputPassword는 "Robbie"이니까
         * */
        boolean matches = passwordEncoder.matches(inputPassword, encodePassword);
        System.out.println("matches = " + matches); // 암호화할 때 사용된 값과 다른 문자열과 비교했기 때문에 false
    }
}