package com.example.Mind_in_Canvas.Security.costomfilter;


import com.example.Mind_in_Canvas.shared.utils.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/auth/login");
    }

    public String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("로그인 필터 아이디 : " + username);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        try {
            return authenticationManager.authenticate(authRequest);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("잘못된 자격 증명입니다. 다시 시도하세요.", ex);
        } catch (LockedException ex) {
            throw new LockedException("계정이 잠겼습니다. 관리자에게 문의하세요.", ex);
        } catch (DisabledException ex) {
            throw new DisabledException("계정이 비활성화되었습니다. 관리자에게 문의하세요.", ex);
        } catch (AccountExpiredException ex) {
            throw new AccountExpiredException("계정이 만료되었습니다. 관리자에게 문의하세요.", ex);
        } catch (CredentialsExpiredException ex) {
            throw new CredentialsExpiredException("자격 증명이 만료되었습니다. 비밀번호를 변경하세요.", ex);
        } catch (AuthenticationException ex) {
            // 다른 모든 인증 예외에 대해 일반적인 메시지 처리
            throw new AuthenticationServiceException("인증 실패: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            jakarta.servlet.FilterChain chain, Authentication authResult) throws IOException {

        // 인증 결과 확인
        System.out.println("인증결과  : " + authResult.isAuthenticated());

        // 역할(권한)을 리스트로 변환
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // JWT 생성
        String token = jwtTokenProvider.createToken(authResult.getName(), roles);

        // JWT를 쿠키에 저장
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // 보안상 true로 설정하는 것이 좋음
        cookie.setSecure(false);    // HTTPS에서만 사용하도록 설정하려면 true로 설정
        cookie.setPath("/");
        response.addCookie(cookie);

        response.sendRedirect("http://localhost:5173/");

//        response.sendRedirect("https://ohmolli.com:9100/swagger-ui/index.html");

        // 인증 성공 시의 로직
//        response.getWriter().write("{\"message\":\"Credential Authentication Success\", " +
//                "\"user\":\"" + obtainUsername(request) + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        // 인코딩 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 401 Unauthorized 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 예외 메시지 처리
        String errorMessage;
        if (failed instanceof BadCredentialsException) {
            errorMessage = failed.getMessage();
        } else if (failed instanceof LockedException) {
            errorMessage = failed.getMessage();
        } else if (failed instanceof DisabledException) {
            errorMessage = failed.getMessage();
        } else if (failed instanceof AccountExpiredException) {
            errorMessage = failed.getMessage();
        } else if (failed instanceof CredentialsExpiredException) {
            errorMessage = failed.getMessage();
        } else {
            errorMessage = failed.getMessage();
        }

        response.getWriter().write("{\"errorMessage\":\"" + errorMessage + "\", " +
                "\"user\":\"" + obtainUsername(request) + "\"}");
    }
}
