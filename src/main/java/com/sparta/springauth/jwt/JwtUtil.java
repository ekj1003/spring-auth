package com.sparta.springauth.jwt;

import com.sparta.springauth.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {// Util : 독립적으로 상속 없이, 하나의 모듈로써 메서드들을 가진 클래스
    // 1. JWT 데이터

    // Header KEY 값, Cookie의 이름
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 KEY <- 권한을 구분하여 가져오기 위함
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; // 구분을 위해 뒤에 한칸 띄운다.

    // 토큰 만료시간 <- 토큰 유지 시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    // Base64 Encode 한 SecretKey, application.properties에서 가져옴
    @Value("${jwt.secret.key}")
    private String secretKey;

    // jwt.secret.key를 관리할 때 사용하는 객체
    private Key key;

    // 암호화 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    // Application이 동작하는 동안에 상태나 동작을 시간순으로 기록하는 것
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // jwt.secret.key가 Base64 인코딩되어있어서 디코딩을 하는 것이다.
        key = Keys.hmacShaKeyFor(bytes);
    }



    // 2. JWT 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();
        // 암호화 되어 JWT 토큰을 만든다.
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간: 현재 시간 + 유지 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 3. 생성된 JWT 토큰을 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
            // BEARER_PREFIX = "Bearer " 띄어쓰기가 있었다.
            // 하지만, Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // 4. Cookie에 들어있던 JWT 토큰을 Substring
    // BEARER_PREFIX을 제거해준다.
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            // 공백과 null 확인 && BEARER_PREFIX로 시작하는 지
            return tokenValue.substring(7); // 앞 7글자인 "Bearer "을 삭제하여 순수한 토큰 값만 반환
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 5. JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // key: 암호화한 key
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            // MalformedJwtException: token의 변조되었을 때
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 6. JWT 토큰에서 사용자 정보 가져오기
    // Body 부분에 Claims가 있다.
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies(); // 담겨있던 쿠키들을 배열로 가져옴.
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                        // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
