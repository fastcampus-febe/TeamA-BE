package com.example.travel.ext;

import com.example.travel.dto.MemberLoginRequest;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Builder
    private JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static JwtAuthenticationFilter of(JwtTokenProvider jwtTokenProvider) {
        return JwtAuthenticationFilter.builder()
                .jwtTokenProvider(jwtTokenProvider)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();

        //filter에서 header를 가져옴
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (!jwtTokenProvider.validateToken(jwtTokenProvider.extractToken(authorizationHeader))){
                response.sendRedirect("/login?redirectURL=" + requestURI);
                throw new Exception();
            }
            //token 값에서 유효값 (id, role)을 추출하여 userDTO를 만듦
            MemberLoginRequest user = jwtTokenProvider.getMemberDtoOf(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user,
                    "",
                    user.getAuthorities()));

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            logger.error("ExpiredJwtException : expired token");
        } catch (Exception exception) {
            logger.error("Exception : no token");
            return ;
        }
    }
}
