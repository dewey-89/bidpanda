package com.panda.back.domain.member.jwt;

import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class TokenProvider {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L;
    private final long RefreshTOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String membername, String nickname) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(membername)
                        .claim("nickname", nickname)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String createRefreshToken(String membername, String nickname) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(membername) // 사용자 식별자값(ID)
                        .claim("nickname", nickname)
                        .setExpiration(new Date(date.getTime() + RefreshTOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT 헤더로 전달
    public void addJwtToHeader(String header, String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            res.addHeader(header,token);

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage()+"헤더로 토큰 전달");
        }
    }

    public String getJwtFromHeader(HttpServletRequest request, String token) {
        String bearerToken = request.getHeader(token);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            //throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.WRONG_JWT_TOKEN);
        }
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
