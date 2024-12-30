package com.example.Mind_in_Canvas.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String email, String password, String role, String socialProvider,
            String phone) {

        if (username != null && email != null && password != null && role != null && socialProvider != null
                && phone != null) {

            // 이미 사용자가 존재하는지 확인
            Optional<UserEntity> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }

            // 새 사용자 생성
            UserEntity user = new UserEntity();
            user.createUser(username, email, password, role, socialProvider, phone, passwordEncoder);
            // 사용자 저장
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("필드가 비어 있습니다.");
        }

    }

    public boolean changePassword(String email, String currentPassword, String newPassword) {
        // 사용자 찾기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(user.getPassword(), newPassword)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호 변경 및 저장
        user.changePassword(newPassword, passwordEncoder);
        userRepository.save(user);
        return true;
    }

    public boolean changeUsername(String email, String newUsername) {
        // 사용자 찾기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 이름 변경 및 저장
        user.changeUsername(newUsername);
        userRepository.save(user);
        return true;
    }

    public boolean changeSocialProvider(String email, String newSocialProvider) {
        // 사용자 찾기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 소셜 프로바이더 변경 및 저장
        user.changeSocialProvider(newSocialProvider);
        userRepository.save(user);
        return true;
    }

    public boolean changePhoneNumber(String email, String newPhoneNumber) {
        // 사용자 찾기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 전화번호 변경 및 저장
        user.changePhoneNumber(newPhoneNumber);
        userRepository.save(user);
        return true;
    }

}
