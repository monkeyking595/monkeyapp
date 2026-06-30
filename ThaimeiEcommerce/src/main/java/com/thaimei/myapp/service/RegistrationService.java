package com.thaimei.myapp.service;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.dto.UserRegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.enums.RoleEnum;
import com.thaimei.myapp.error.AppException;


@Service
public class RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public RegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private  User  BuildUser (UserRegistrationDto dto, RoleEnum role) {
    User user=new User();
    user.setUsername(dto.getUsername());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setEmail(dto.getEmail());
    user.setRole(role);
    return user;
    }

    private void  validateUser(UserRegistrationDto dto) {
        if(!dto.getPassword().equals(dto.getConfirmpassword())) {
            throw new AppException("password does not match!",400);
        }
        if (userService.existsByUsername(dto.getUsername())) {
            throw new AppException ("Username already exists!", 409);
        }
    }

    public void registerUser(UserRegistrationDto dto) {
        validateUser(dto);
        userService.save(BuildUser(dto, RoleEnum.CUSTOMER));
    }

    public void registerSeller(UserRegistrationDto dto) {
        validateUser(dto);
        userService.save(BuildUser(dto, RoleEnum.SELLER));
    }

    public void registerAdmin(UserRegistrationDto dto) {
        validateUser(dto);
        userService.save(BuildUser(dto, RoleEnum.ADMIN));
    }
}
