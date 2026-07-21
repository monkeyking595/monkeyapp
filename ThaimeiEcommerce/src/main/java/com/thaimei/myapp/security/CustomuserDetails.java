package com.thaimei.myapp.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import com.thaimei.myapp.model.User;  
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List; 
import com.thaimei.myapp.enums.UserStatus;

public class CustomUserDetails implements UserDetails {
   
    private final User user;
    public CustomUserDetails(User user) {
        this.user=user;
    }

    public User getUser() {
        return user;
    }
    
    public Long getId() {
        return user.getId();
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    // set the user to active until i want to deliberately unactivate it.
    @Override
    public boolean isAccountNonExpired() {
        return user.getStatus() == UserStatus.ACTIVE;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
