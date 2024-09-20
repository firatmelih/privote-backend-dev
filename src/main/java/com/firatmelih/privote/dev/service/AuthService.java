package com.firatmelih.privote.dev.service;

import com.firatmelih.privote.dev.model.Role;
import com.firatmelih.privote.dev.model.User;
import com.firatmelih.privote.dev.repository.UserRepository;
import com.firatmelih.privote.dev.request.AuthRequest;
import com.firatmelih.privote.dev.request.RegisterRequest;
import com.firatmelih.privote.dev.response.AuthResponse;
import com.firatmelih.privote.dev.security.JwtService;
import com.firatmelih.privote.dev.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        var jwtUser = userDetailsService.loadUserByUsername(authRequest.getUsername());
        var userId = userDetailsService.getUserId(authRequest.getUsername());
        var jwtToken = jwtService.generateToken(jwtUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .user_id(userId)
                .build();
    }

    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        var user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        UserDetails jwtUser = userDetailsService.loadUserByUsername(user.getUsername());

        var jwtToken = jwtService.generateToken(jwtUser);
        AuthResponse response = AuthResponse.builder()
                .token(jwtToken)
                .user_id(user.getId())
                .message("User successfully registered!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
