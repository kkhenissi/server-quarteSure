package com.fdc.quartesure.quartesurebackend.JWT;
 import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/user/login"),
                                new AntPathRequestMatcher("/user/login"),
                                new AntPathRequestMatcher("/api/user/signup"),
                                new AntPathRequestMatcher("/user/signup"),
                                new AntPathRequestMatcher("/api/user/forgot-password"),
                                new AntPathRequestMatcher("/user/forgot-password"),
                                new AntPathRequestMatcher("/api/user/reset-password"),
                                new AntPathRequestMatcher("/user/reset-password"),
                                new AntPathRequestMatcher("/api/user/activate"),
                                new AntPathRequestMatcher("/user/activate"),
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/v3/api-docs/**"),
                                new AntPathRequestMatcher("/api/stripe/**")
                              //  new AntPathRequestMatcher("/stripe/**")
                        ).permitAll()
                         .anyRequest().authenticated()
                );


        //  AJOUT DU FILTRE JWT (OBLIGATOIRE)
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Ceci rend AuthenticationManager injectable partout
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
