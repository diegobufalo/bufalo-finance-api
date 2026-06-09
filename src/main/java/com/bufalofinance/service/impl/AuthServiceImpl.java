package com.bufalofinance.service.impl;

import com.bufalofinance.dto.request.LoginRequest;
import com.bufalofinance.dto.request.RegisterRequest;
import com.bufalofinance.dto.response.AuthResponse;
import com.bufalofinance.entity.InviteCode;
import com.bufalofinance.entity.User;
import com.bufalofinance.repository.InviteCodeRepository;
import com.bufalofinance.repository.UserRepository;
import com.bufalofinance.security.JwtService;
import com.bufalofinance.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final InviteCodeRepository inviteCodeRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        InviteCode invite = inviteCodeRepository.findByCodeAndUsedByIsNull(req.inviteCode())
                .filter(i -> i.getExpiresAt() == null || i.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new IllegalArgumentException("Código de convite inválido ou expirado"));

        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setInvitedBy(invite.getCreatedBy());
        user = userRepository.save(user);

        invite.setUsedBy(user.getId());
        invite.setUsedAt(Instant.now());
        inviteCodeRepository.save(invite);

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (user.getPasswordHash() == null || !passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }
}
