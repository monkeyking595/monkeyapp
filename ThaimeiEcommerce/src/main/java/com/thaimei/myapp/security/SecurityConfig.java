package com.thaimei.myapp.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, JwtAuthenticationEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint; 
    }
        
    @Bean
    public SecurityFilterChain SecurityConfigSecurityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf->csrf.disable())
        //here the configurer object (sessionManagementConfigurer) is passed in to the lambda 
        //lambda implements the functional interface (customizer) which has the customize() method which takes the configurer object and configures it with STATELESS
        //this happens the same for the all the other configurers in the code below
        .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthEntryPoint))
        .authorizeHttpRequests(auth->auth.requestMatchers("/","/signup","/login_page","/error","/login","/css/**","/js/**","/myimages/**","/myapp.css","/account.css").permitAll()
        .requestMatchers("/admin/api/adminlogin").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); 
    return http.build();
    }
    //Program to interface, not the implementation method is used here, where the interface is use as the return type.
    //it gives more flexibility
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    // AuthenticationConfiguration configures everything that is needed by the authenticationManager
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
