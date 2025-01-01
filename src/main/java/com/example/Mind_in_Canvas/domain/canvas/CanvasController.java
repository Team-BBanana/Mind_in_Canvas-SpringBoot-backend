package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.dto.ErrorResponse;
import com.example.Mind_in_Canvas.dto.canvas.*;
import com.example.Mind_in_Canvas.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/canvas")
@Slf4j
public class CanvasController {


    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    private final CanvasService canvasService;

    public CanvasController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

   @PostMapping("/createCanvas")
    public ResponseEntity<?> createCanvas(@CookieValue(value = "kidjwt", required = false) String token, @RequestBody CreateCanvasRequest request) {
        try {
            // Create a new canvas and send a request to the external service
            CreateCanvasResponse res =  canvasService.createCanvas(token,request.getTitle());
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}