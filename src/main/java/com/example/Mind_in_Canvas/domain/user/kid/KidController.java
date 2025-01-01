package com.example.Mind_in_Canvas.domain.user.kid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Mind_in_Canvas.dto.ErrorResponse;
import com.example.Mind_in_Canvas.dto.user.KidDTO;
import com.example.Mind_in_Canvas.domain.canvas.CanvasService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/kids")
public class KidController {

    private final KidService kidService;
    private final CanvasService canvasService;

    @Autowired
    public KidController(KidService kidService, CanvasService canvasService) {
        this.kidService = kidService;
        this.canvasService = canvasService;
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
