package com.example.investment.testsupport;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureTestController {
    @GetMapping("/secure/me")
    public Long me(Authentication authentication) {
        // JwtAuthFilter가 principal을 userId로 넣어둠
        return (Long) authentication.getPrincipal();
    }
}
