package com.example.Mind_in_Canvas.domain.user.parent;

import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import com.example.Mind_in_Canvas.domain.user.kid.KidRepository;
import com.example.Mind_in_Canvas.dto.user.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KidRepository kidRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(String username, String email, String password, String role, String socialProvider , String phone ) {

        if( username != null && email != null && password != null && role != null && socialProvider != null && phone != null ) {

            // 이미 사용자가 존재하는지 확인
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }

            // 새 사용자 생성
            User user = new User();
            user.createUser(username,email,password,role,socialProvider,phone, passwordEncoder);
            // 사용자 저장
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("필드가 비어 있습니다.");
        }
    }

    @Transactional
    public UpdateUserResDto updateUser(UUID userId, UpdateUserReqDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User.UserBuilder userBuilder = User.builder()
                .parentId(user.getParentId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .socialProvider(user.getSocialProvider());

        Optional.ofNullable(dto.getPassword())
                .ifPresent(password -> userBuilder.password(passwordEncoder.encode(password)));

        Optional.ofNullable(dto.getPhoneNumber())
                .ifPresent(userBuilder::phoneNumber);

        userRepository.save(user);
        return new UpdateUserResDto(user);
    }

    @Transactional
    public UpdateKidResDto updateKid(String userEmail, UUID kidId, UpdateKidReqDto dto) {
        User parent = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        Kid kid = parent.getKids().stream()
                .filter(k -> k.getKidId().equals(kidId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Kid not found"));

        Kid updatedKid = Kid.builder()
                .kidId(kid.getKidId())
                .parent(parent)
                .name(dto.getName())
                .age(dto.getAge())
                .build();

        kidRepository.save(updatedKid);
        return UpdateKidResDto.from(updatedKid);
    }

    @Transactional
    public CreateKidResDto addKid(UUID parentId, CreateKidReqDto dto) {
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        Kid kid = Kid.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .parent(parent)
                .build();

        Kid savedKid = kidRepository.save(kid);
        parent.addKid(savedKid);

        return CreateKidResDto.from(savedKid);
    }
}
