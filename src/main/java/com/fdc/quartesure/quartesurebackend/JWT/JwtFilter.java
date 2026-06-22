package com.fdc.quartesure.quartesurebackend.JWT;

import com.fdc.quartesure.quartesurebackend.JWT.CustomerUsersDetailsService;
import com.fdc.quartesure.quartesurebackend.JWT.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUsersDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // 🚨 AJOUT ESSENTIEL : exclure les webhooks Stripe
        if (path.equals("/stripe/webhook")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔓 Laisse passer tes routes publiques actuelles (inchangées)
        if (path.equals("/user/login")
                || path.equals("/user/signup")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 Vérification du JWT
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String username = jwtUtil.extractUserName(token);

                if (username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                    filterChain.doFilter(request, response);
                }
            } catch (Exception ignored) {
                // Ne jamais bloquer un JWT invalide
            }
        }

        // ➜ Toujours continuer

    }
}
