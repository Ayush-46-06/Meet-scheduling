package com.meet.MeetSchedulling.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.meet.MeetSchedulling.security.OAuthSuccessHandler;
@Configuration
public class SecurityConfig {

    @Autowired
    private OAuthSuccessHandler successHandler;

    @org.springframework.beans.factory.annotation.Value("${frontend.url}")
    private String frontendUrl;

   @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(org.springframework.security.config.Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/login**").permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth -> oauth
            .successHandler(successHandler)
            .failureHandler((request, response, exception) -> {
                String errorMsg = exception.getMessage();
                if (errorMsg == null) {
                    errorMsg = "Unknown error";
                }
                response.sendRedirect("/login?error=" + java.net.URLEncoder.encode(errorMsg, "UTF-8"));
            })
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
                .logoutSuccessUrl(frontendUrl)
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
        );

    return http.build();
}
}