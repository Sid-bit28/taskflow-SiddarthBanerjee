package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}
