package com.thaimei.myapp.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class AppConfigs {
    @Bean 
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
}
