package com.example.Mind_in_Canvas.config;

import com.example.Mind_in_Canvas.Security.CustomAuthenticationEntryPoint;
import com.example.Mind_in_Canvas.Security.costomfilter.*;
import com.example.Mind_in_Canvas.shared.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomOauthFailureHandler customOauthFailureHandler() {
        return new CustomOauthFailureHandler();
    }

    @Autowired
    private CustomOidcLogoutSuccessHandler customOidcLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration, JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtTokenProvider);
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, userDetailsService);
        CustomOauthSuccessHandler customOauthSuccessHandler = new CustomOauthSuccessHandler(userDetailsService,jwtTokenProvider);

        http
                .securityMatcher("/**")
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션을 사용하지 않음
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/login**", "/error**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                )
                // .formLogin(formLogin -> formLogin
                //         .loginPage("/users/login") // 사용자 정의 로그인 페이지 경로
                //         .permitAll() // 로그인 페이지는 모두 접근 가능
                // )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .deleteCookies("jwt")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customOauthSuccessHandler)  // 로그인 성공 시 처리
                        .failureHandler(customOauthFailureHandler())  // 로그인 실패 시 처리
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(customOidcLogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .deleteCookies("jwt")
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)  // 인증 실패 시 처리
                );


        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
