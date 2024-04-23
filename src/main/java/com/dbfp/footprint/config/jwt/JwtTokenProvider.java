package com.dbfp.footprint.config.jwt;

import com.dbfp.footprint.api.service.member.CustomUserDetailsService;
import com.dbfp.footprint.config.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    private final CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds, CustomUserDetailsService userDetailsService) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("memberId", userDetails.getId())
                .claim("type", "access")
                .signWith((SecretKey) key, Jwts.SIG.HS512)
                .expiration(validity)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        long refreshTokenValidityInMilliSeconds = 60480000;
        long now = (new Date()).getTime();
        Date validity = new Date(now + refreshTokenValidityInMilliSeconds);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("memberId", userDetails.getId())
                .claim("type", "refresh")
                .signWith((SecretKey) key, Jwts.SIG.HS512)
                .expiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, token, new ArrayList<>());
    }

    public boolean validateToken(String token) {
        try {
            Claims payload = Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload();

            String tokenType = payload.get("type", String.class);
            if ("refresh".equals(tokenType)) {
                return false;
            }
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.info("잘못된 토큰 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("비정상적인 토큰입니다.");
        }
        return false;
    }

    public Date getAccessTokenExpire(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }
}
