package com.panda.back.domain.member.jwt;

import com.panda.back.domain.member.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberDetailsServiceImpl memberDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = tokenProvider.getJwtFromHeader(req,TokenProvider.AUTHORIZATION_HEADER);
        String refreshToken = tokenProvider.getJwtFromHeader(req,TokenProvider.REFRESH_HEADER);

        if (StringUtils.hasText(accessToken)) {

            if (!tokenProvider.validateToken(accessToken)) {
                String refresh = req.getHeader(tokenProvider.REFRESH_HEADER);
                if (!tokenProvider.validateToken(refreshToken) || !refreshTokenRepository.existsByToken(refresh)){
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    log.info("Token이 만료 되었습니다.");
                    throw new JwtException("Refresh Token Error");
                }

                log.info("Access Token reCreate");
                Claims info = tokenProvider.getUserInfoFromToken(refreshToken);
                String membername = info.getSubject();
                String nickname = info.get("nickname",String.class);

                accessToken = tokenProvider.createToken(membername,nickname);
                res.addHeader(TokenProvider.AUTHORIZATION_HEADER, accessToken);
                accessToken = tokenProvider.substringToken(accessToken);
                log.info("accessToken 재생성");
            }

            Claims info = tokenProvider.getUserInfoFromToken(accessToken);

            log.info("Token Authorization");
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String membername) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(membername);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String membername) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(membername);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
