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

    public void createUser(String username, String email, String password, String role, String socialProvider , String phone ) {

        if( username != null && email != null && password != null && role != null && socialProvider != null && phone != null ) {

            // 이미 사용자가 존재하는지 확인
            Optional<UserEntity> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }

            // 새 사용자 생성
            UserEntity user = new UserEntity();
            user.createUser(username,email,password,role,socialProvider,phone, passwordEncoder);
            // 사용자 저장
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("필드가 비어 있습니다.");
        }

    }

}
