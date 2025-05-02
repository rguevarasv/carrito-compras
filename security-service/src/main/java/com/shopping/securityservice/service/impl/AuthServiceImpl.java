package com.shopping.securityservice.service.impl;

import com.shopping.securityservice.dto.AuthRequestDto;
import com.shopping.securityservice.dto.AuthResponseDto;
import com.shopping.securityservice.dto.RegisterRequestDto;
import com.shopping.securityservice.entity.Role;
import com.shopping.securityservice.entity.User;
import com.shopping.securityservice.exception.UserAlreadyExistsException;
import com.shopping.securityservice.repository.UserRepository;
import com.shopping.securityservice.service.AuthService;
import com.shopping.securityservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Create new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        // Generate JWT token
        String jwt = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(jwt)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponseDto authenticate(AuthRequestDto request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Generate JWT token
        String jwt = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(jwt)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}