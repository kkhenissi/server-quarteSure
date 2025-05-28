package com.fdc.quartesure.quartesurebackend.JWT;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomerUserDetails) {
            return ((CustomerUserDetails) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return principal.toString();
        }

        throw new IllegalStateException("Utilisateur non authentifié ou principal inconnu.");
    }
}
