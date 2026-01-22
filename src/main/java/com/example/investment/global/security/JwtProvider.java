package com.example.investment.global.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtProvider {
    private final Key key;
    private final long expireMs=1000L*60*60;
    public JwtProvider(@Value("${jwt.secret}")String secret){
        this.key= Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String createToken(Long userId){
        Date now=new Date();
        return Jwts.builder().setSubject(String.valueOf(userId)).setIssuedAt(now).setExpiration(new Date(now.getTime()+expireMs)).signWith(key, SignatureAlgorithm.HS256).compact();
    }
    public Long getUserId(String token){
        Claims claims=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }
}
