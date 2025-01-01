package com.example.Mind_in_Canvas.domain.user.kid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Mind_in_Canvas.dto.ErrorResponse;
import com.example.Mind_in_Canvas.dto.user.KidDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kids")
public class KidController {

    private final KidService kidService;

    @Autowired
    public KidController(KidService kidService) {
        this.kidService = kidService;
    }

    @PostMapping("/generateKidToken")
    public ResponseEntity<?> generateKidToken(@CookieValue(value = "jwt", required = false) String token, @RequestBody Map<String, String> requestBody, HttpServletResponse response) {
        try {
            String kidId = requestBody.get("kidId");
            String kidToken = kidService.generateKidToken(token, kidId);
            Cookie cookie = new Cookie("kidjwt", kidToken);
            cookie.setHttpOnly(true); // 보안상 true로 설정하는 것이 좋음
            cookie.setSecure(false);  // HTTPS에서만 사용하도록 설정하려면 true로 설정
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(kidToken);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Conflict", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
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
            System.out.println("token: " + token);
            List<Kid> kids = kidService.findMyKids(token);
            return ResponseEntity.ok(kids);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse("Conflict", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

}
