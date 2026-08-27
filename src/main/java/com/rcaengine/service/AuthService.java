package com.rcaengine.service;

import com.rcaengine.dto.LoginRequest;
import com.rcaengine.dto.LoginResponse;
import com.rcaengine.entity.User;
import com.rcaengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole()
        );

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole()
        );
    }
}