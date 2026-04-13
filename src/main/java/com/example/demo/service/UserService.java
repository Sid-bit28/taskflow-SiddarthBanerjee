package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // BCrypt encoder (Cost = 12)
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User registerUser(String name, String email, String rawPassword) {
        // Need to check if the email already exists using userRepository.findByEmail()
        if(userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        User user = User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .build();
        return userRepository.save(user);
    }

    public String loginUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!bCryptPasswordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }
}
