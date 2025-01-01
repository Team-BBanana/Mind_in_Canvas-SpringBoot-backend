package com.example.Mind_in_Canvas.Security.costomfilter;

import com.example.Mind_in_Canvas.shared.utils.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    private String frontendUrl = "http://localhost:5173";
    private String endPoint = "/selectkids";

    // Constructor injection
    public CustomOauthSuccessHandler(UserDetailsService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            // OAuth2 인증 후 성공 처리 로직
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            System.out.println("소셜인증 성공시 : " + oauth2User.getName());

            // 이메일 가져오기
            String email = oauth2User.getAttribute("email");

            // UserDetailsService를 사용하여 사용자 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 사용자 상태 확인 및 예외 던지기
            checkUserStatus(userDetails);

            System.out.println("소셜회원 조회 : " + userDetails.getUsername());

            // 역할(권한)을 List<String>으로 변환
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // JWT 생성
            String token = jwtTokenProvider.createToken(userDetails.getUsername(), roles);
            System.out.println("소셜회원 토큰  : " + token);

            // JWT를 쿠키에 저장
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true); // 보안상 true로 설정하는 것이 좋음
            cookie.setSecure(false);    // HTTPS에서만 사용하도록 설정하려면 true로 설정
            cookie.setPath("/");
            response.addCookie(cookie);
    
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK); // 302 OK
            System.out.println("프론트엔드 루트 URL로 리디렉션 : " + frontendUrl + endPoint);
            // 프론트엔드 루트 URL로 리디렉션
            response.sendRedirect(frontendUrl + endPoint);

        }catch (UsernameNotFoundException ex){
            handleException(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        }catch (LockedException ex) {
            handleException(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (DisabledException ex) {
            handleException(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (AccountExpiredException ex) {
            handleException(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (CredentialsExpiredException ex) {
            handleException(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            handleException(response, ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void checkUserStatus(UserDetails userDetails) {
        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("계정이 잠겨 있습니다.");
        }
        if (!userDetails.isEnabled()) {
            throw new DisabledException("계정이 비활성화되었습니다.");
        }
        if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("계정이 만료되었습니다.");
        }
        if (!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("자격 증명이 만료되었습니다.");
        }
    }

    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"errorMessage\":\"" + message + "\"}");
    }
}
