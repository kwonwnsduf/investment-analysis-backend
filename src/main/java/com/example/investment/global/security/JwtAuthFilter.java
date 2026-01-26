package com.example.investment.global.security;

import com.example.investment.domain.user.User;
import com.example.investment.domain.user.UserRepository;
import com.example.investment.global.exception.ApiException;
import com.example.investment.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider  jwtProvider;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
        String header=request.getHeader("Authorization");
        if(header!=null&&header.startsWith("Bearer ")){
            String token=header.substring(7);
            Long userId=jwtProvider.getUserId(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            //AuthUser authUser = new AuthUser(user);
            Authentication auth=new UsernamePasswordAuthenticationToken(userId,null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request,response);
    }
}
