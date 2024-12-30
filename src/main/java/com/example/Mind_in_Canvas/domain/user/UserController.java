package com.example.Mind_in_Canvas.domain.user;

import com.example.Mind_in_Canvas.dto.UserDTO;
import com.example.Mind_in_Canvas.dto.UserDTOchangePassword;
import com.example.Mind_in_Canvas.dto.UserDTOchangeUsername;
import com.example.Mind_in_Canvas.dto.UserDTOchangeSocial;
import com.example.Mind_in_Canvas.dto.UserDTOchangePhone;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserDTO userDTO) {

        try {
            userService.createUser(
                    userDTO.getName(),
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getRole(),
                    userDTO.getSocialProvider(),
                    userDTO.getPhoneNumber());

            Map<String, String> response = new HashMap<>();
            response.put("message", "User Create Success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PostMapping("/changepassword")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody UserDTOchangePassword userDTOchangePassword) {
        boolean result = userService.changePassword(
                userDTOchangePassword.getEmail(),
                userDTOchangePassword.getPassword(),
                userDTOchangePassword.getNewPassword());

        if (result) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "비밀번호 변경에 성공하였습니다");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "비밀번호 변경에 실패하였습니다");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PostMapping("/changeusername")
    public ResponseEntity<Map<String, String>> changeUsername(
            @Valid @RequestBody UserDTOchangeUsername userDTOchangeUsername) {
        boolean result = userService.changeUsername(
                userDTOchangeUsername.getEmail(),
                userDTOchangeUsername.getName());

        Map<String, String> response = new HashMap<>();
        if (result) {
            response.put("message", "사용자 이름이 변경되었습니다");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "사용자 이름 변경에 실패했습니다");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/changesocial")
    public ResponseEntity<Map<String, String>> changeSocial(
            @Valid @RequestBody UserDTOchangeSocial userDTOchangeSocial) {
        boolean result = userService.changeSocialProvider(
                userDTOchangeSocial.getEmail(),
                userDTOchangeSocial.getSocialProvider());

        if (result) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "소셜 프로바이더가 변경되었습니다");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "소셜 프로바이더 변경에 실패하였습니다");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PostMapping("/changephonenumber")
    public ResponseEntity<Map<String, String>> changePhoneNumber(
            @Valid @RequestBody UserDTOchangePhone userDTOchangePhone) {
        boolean result = userService.changePhoneNumber(
                userDTOchangePhone.getEmail(),
                userDTOchangePhone.getPhoneNumber());

        if (result) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "전화번호가 변경되었습니다");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "전화번호 변경에 실패하였습니다");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

}
