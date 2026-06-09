package com.bufalofinance.service;

import com.bufalofinance.dto.request.LoginRequest;
import com.bufalofinance.dto.request.RegisterRequest;
import com.bufalofinance.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
