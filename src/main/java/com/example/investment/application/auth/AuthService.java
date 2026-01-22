package com.example.investment.application.auth;

import com.example.investment.domain.user.User;
import com.example.investment.domain.user.UserRepository;
import com.example.investment.global.exception.ApiException;
import com.example.investment.global.exception.ErrorCode;
import com.example.investment.global.security.JwtProvider;
import com.example.investment.presentation.auth.dto.LoginRequest;
import com.example.investment.presentation.auth.dto.LoginResponse;
import com.example.investment.presentation.auth.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    public void signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }
        User user = User.builder()
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .nickname(req.nickname())
                .build();
        userRepository.save(user);}
    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }

        String token = jwtProvider.createToken(user.getId());
        return new LoginResponse(token);
    }
}
