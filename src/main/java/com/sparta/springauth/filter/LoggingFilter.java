package com.sparta.springauth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "LoggingFilter") // LoggingFilter로 로깅이 찍힌다.
@Component
@Order(1)
public class LoggingFilter implements Filter { // 1. Filter 거침
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // FilterChain chain: filter를 이동할 때 사용

        // 전처리
        //
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();
        log.info(url); // 2. 어떤 요청인지 로그를 찍어줌

        chain.doFilter(request, response); // 3. 다음 Filter 로 이동
        // 4. DispatcherServlet으로 이동
        // 5. Handler Mapping을 통해 Controller 수행
        // 6. DispatcherServlet으로 응답
        // 7. View 반환
        // 8. DispatcherServlet에서 Filter로 응답을 보냄.
        // 9. Filter에서 후처리
        log.info("비즈니스 로직 완료"); // 10. 응답 로그 찍어줌
    }
}