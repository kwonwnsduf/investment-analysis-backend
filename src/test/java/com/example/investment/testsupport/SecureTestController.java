package com.example.investment.testsupport;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureTestController {
    @GetMapping("/secure/me")
    public String me(@AuthenticationPrincipal Long userId) {
        // JwtAuthFilter가 principal을 userId로 넣어둠
        return String.valueOf(userId);
    }
}
