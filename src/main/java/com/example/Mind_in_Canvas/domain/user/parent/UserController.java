package com.example.Mind_in_Canvas.domain.user.parent;

import com.example.Mind_in_Canvas.dto.user.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/api/users")
public class UserController {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserDTO userDTO) {

        try {
            userService.createUser(
                    userDTO.getName(),
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getRole(),
                    userDTO.getSocialProvider(),
                    userDTO.getPhoneNumber()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "User Create Success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UpdateUserResDto> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserReqDto reqDto) {
        return ResponseEntity.ok(userService.updateUser(userId, reqDto));
    }

    @PatchMapping("/{kidId}")
    public ResponseEntity<UpdateKidResDto> updateKid(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID kidId,
            @Valid @RequestBody UpdateKidReqDto reqDto) {
        return ResponseEntity.ok(userService.updateKid(userDetails.getUsername(), kidId, reqDto));
    }

    @PostMapping
    public ResponseEntity<CreateKidResDto> createKid(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateKidReqDto reqDto) {
        UUID parentId = UUID.fromString(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addKid(parentId, reqDto));
    }
}
