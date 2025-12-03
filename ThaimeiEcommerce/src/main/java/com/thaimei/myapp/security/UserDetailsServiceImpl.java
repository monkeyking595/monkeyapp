package com.thaimei.myapp.security;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.repository.UserRepository;
import com.thaimei.myapp.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
       User user = userRepository.findByUsername(username)
       .orElseThrow(() -> new UsernameNotFoundException ("Username not found"));
    return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long userId) {
        User user= userRepository.findById(userId)
        .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        return new CustomUserDetails(user);
    }

} 
