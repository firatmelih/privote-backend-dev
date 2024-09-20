package com.firatmelih.privote.dev.controller;

import com.firatmelih.privote.dev.repository.PollRepository;
import com.firatmelih.privote.dev.request.AuthRequest;
import com.firatmelih.privote.dev.request.RegisterRequest;
import com.firatmelih.privote.dev.response.AuthResponse;
import com.firatmelih.privote.dev.service.AuthService;
import com.firatmelih.privote.dev.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PollRepository pollRepository;
    private final PollService pollService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }
}
