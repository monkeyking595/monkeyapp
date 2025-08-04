package com.thaimei.myapp.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    
    @Bean
    public SecurityFilterChain SecurityConfigsecurityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/your_account","/css/**","/images/**").permitAll()
        .anyRequest().authenticated()
        )
        .formLogin(form -> form
        .loginPage("/login_page")
        .defaultSuccessUrl("/index", true)
        .permitAll()
        )
        .logout(logout -> logout
        .logoutSuccessUrl("/login?logout")
        .permitAll()
        );
    return http.build();
        
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
