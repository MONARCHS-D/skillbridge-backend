package com.monarchs.SkillBridge.secutity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt=authHeader.substring(7);

        try {
            Claims claims= jwtUtil.parseClaims(jwt);
            String userId= claims.getSubject();

            if(userId!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                String role=claims.get("role",String.class);

                List<GrantedAuthority> authorities=List.of(new SimpleGrantedAuthority("ROLE_"+role));

                UsernamePasswordAuthenticationToken authToken=
                        new UsernamePasswordAuthenticationToken(userId,null,authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (ExpiredJwtException e) {
            log.info("Jwt token expired for request [{}]",request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (JwtException e) {
            log.error("Invalid jwt for request [{}]: {}",request.getRequestURI(),e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        catch (Exception e) {
            log.error("JWT Auth failed for request [{}]: {}",request.getRequestURI(),e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path=request.getRequestURI();
        return path.startsWith("/api/v1/skillbridge/user/")
                || path.startsWith("/api/v2/skillbridge/auth/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");

    }
}
