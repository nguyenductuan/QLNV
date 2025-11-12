package com.edu.qlda.controller;

import com.edu.qlda.jwt.JwtTokenUtil;
import com.edu.qlda.playload.response.Messageresponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController

@CrossOrigin("http://localhost:4200")
@Tag(name = "Gen  Controller", description = "API sinh token")
public class authenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(@RequestParam Long userId) {
        String token = jwtTokenUtil.generateToken(userId);
        log.info("Token tạo ra: {}", token);
        return  ResponseEntity.ok(new Messageresponse<>(200,"Thành công",token));

    }
}
