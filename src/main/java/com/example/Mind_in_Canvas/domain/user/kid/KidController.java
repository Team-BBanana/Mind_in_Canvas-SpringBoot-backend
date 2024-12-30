package com.example.Mind_in_Canvas.domain.user.kid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Mind_in_Canvas.dto.ErrorResponse;
import com.example.Mind_in_Canvas.dto.user.KidDTO;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/kids")
public class KidController {

    private final KidService kidService;

    @Autowired
    public KidController(KidService kidService) {

        this.kidService = kidService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody KidDTO kidDTO, @CookieValue(value = "jwt", required = false) String token) {
        try {
            System.out.println("Received KidDTO with name: " + kidDTO.getName() + ", age: " + kidDTO.getAge());

            kidService.createKid(kidDTO.getName(), kidDTO.getAge(), token);

            return ResponseEntity.ok("Kid Create Success");

        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse("Conflict", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/mykids")
    public ResponseEntity<?> getMyKids(@CookieValue(value = "jwt", required = false) String token) {
        try {
            // Kid 리스트 가져오기
            System.out.println("token: " + token);
            List<Kid> kids = kidService.findMyKids(token);

            // JSON 응답 반환
            return ResponseEntity.ok(kids);

        } catch (IllegalArgumentException e) {
            // 에러 메시지를 JSON 형태로 반환
            ErrorResponse errorResponse = new ErrorResponse("Conflict", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
}
