package com.shopping.securityservice.service;

import com.shopping.securityservice.dto.AuthRequestDto;
import com.shopping.securityservice.dto.AuthResponseDto;
import com.shopping.securityservice.dto.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto authenticate(AuthRequestDto request);
}