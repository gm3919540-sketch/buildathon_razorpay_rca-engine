package com.rcaengine.config;

import com.rcaengine.entity.User;
import com.rcaengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User user = new User(
                "admin",
                passwordEncoder.encode("admin123"),
                "ADMIN"
        );

        userRepository.save(user);
    }
}