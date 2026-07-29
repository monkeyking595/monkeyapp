package com.thaimei.myapp.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thaimei.myapp.enums.RoleEnum;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.repository.UserRepository;

@Configuration
public class DataSeeder {
    @Bean
    //seed the admin's data 
    CommandLineRunner seedAdmin(UserRepository repo,
        PasswordEncoder encoder,
        //gets the values from Spring's environment
        @Value("${admin.seed.email}") String adminEmail,
        @Value("${admin.seed.username}") String adminUsername,
        @Value("${admin.seed.password}") String adminPassword) {

        return args -> {
            if(repo.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(encoder.encode(adminPassword));
            admin.setRole(RoleEnum.ADMIN);
            repo.save(admin);
            }
        
        };
    }
}
