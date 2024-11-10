package com.fdc.quartesure.quartesurebackend.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUsersDetailsService service;

    Claims claims = null;
    private  String userName = null;

    public  boolean isAdmin() {
        log.info("inside isAdmin {}",claims.get("role"));
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public  boolean isUser() {
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser() {
        return userName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().matches(("/user/login|/user/activate|/currentcourse/participants|/user/forgotPassword|/user/signup|/h2-console"))) {
           //  || request.getServletPath().contains("/user/updateSelectedOptions/")
            filterChain.doFilter(request,response);
        } else {
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;

            System.out.println("€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€");
            System.out.println(authorizationHeader);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUserName(token);
                claims = jwtUtil.extractAllClaims(token);
                log.info("What is in claims here==>{}",claims);

            }
            if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
                UserDetails userDetails = service.loadUserByUsername(userName);

                if(jwtUtil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                ObjectMapper mapper = new ObjectMapper();
                //  User localUser = mapper.readValue(request.getInputStream(), User.class);
                System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°");

                System.out.println(userDetails);
                System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°");

            }

         filterChain.doFilter(request,response);
        }
    }
}
