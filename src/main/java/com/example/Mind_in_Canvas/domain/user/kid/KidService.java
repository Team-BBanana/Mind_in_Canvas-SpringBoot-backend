package com.example.Mind_in_Canvas.domain.user.kid;

import com.example.Mind_in_Canvas.domain.user.parent.User;
import com.example.Mind_in_Canvas.domain.user.parent.UserRepository;
import com.example.Mind_in_Canvas.shared.utils.JwtTokenProvider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KidService {

    private final KidRepository kidRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public KidService(KidRepository kidRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.kidRepository = kidRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generateKidToken(String token, String kidId) {
        List<String> userRole = jwtTokenProvider.getUserRole(token);
        if (!userRole.contains("ROLE_USER")) {
            throw new IllegalArgumentException("User is not a parent");
        }

        String kidToken = jwtTokenProvider.createToken(kidId, userRole);

        return kidToken;
    }


    public void createKid(String name, Integer age, String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
        String userEmail = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        System.out.println("Creating Kid with name: " + name + ", age: " + age);

        Kid kid = new Kid(user, name, age);
        kidRepository.save(kid);
    }

    public List<Kid> findMyKids(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

        String userEmail = jwtTokenProvider.getUsername(token);
        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid token: unable to extract email");
        }

        List<Kid> kids = kidRepository.findAllByParentEmail(userEmail);
        if (kids.isEmpty()) {
            throw new IllegalArgumentException("No kids found for the given user");
        }

        return kids;
    }
}