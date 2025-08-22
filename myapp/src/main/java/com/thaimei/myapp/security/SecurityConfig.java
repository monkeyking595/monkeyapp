package com.thaimei.myapp.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SecurityConfig {

    
    @Bean
    public SecurityFilterChain SecurityConfigsecurityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/signup","login_page","/css/**","/images/**").permitAll()
        .requestMatchers("/index", "/profile","/about","/contact").authenticated()
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
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    

    
}
