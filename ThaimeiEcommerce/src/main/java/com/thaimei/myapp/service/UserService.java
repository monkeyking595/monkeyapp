package com.thaimei.myapp.service;
import com.thaimei.myapp.dto.adminDto.AdminUserDto;
import com.thaimei.myapp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import com.thaimei.myapp.enums.RoleEnum;



@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void save(User user) {
        if (user != null) {
            userRepository.save(user);
        }
        
    }

    public User findById(long id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
        .orElseThrow(()-> new RuntimeException ("user not found"));
    }

    public Slice<AdminUserDto> getUserByRole(RoleEnum role, Pageable pageable) {
       return  userRepository.findByRole(role,pageable)
       .map(user -> modelMapper.map(user, AdminUserDto.class));
    }
    
    
}
