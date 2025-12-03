package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import com.thaimei.myapp.dto.UserInfoDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.service.UserProfileService;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RequestMapping
@RestController
public class UserProfileController {
     
     private final UserProfileService userProfileService;
     private final UserService userService;
     public UserProfileController(UserProfileService userProfileService, UserService userService) {
          this.userProfileService = userProfileService;
          this.userService = userService;
     }
    
    //fetch user info from the DB
    @GetMapping("/profile-info")
    public ResponseEntity<UserInfoDto> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails)  {
            User currentUser = userService.findById(userDetails.getId());
            UserInfoDto userInfoDto = userProfileService.getprofileByUser(currentUser);
            return ResponseEntity.ok(userInfoDto);
    }
    //Save or update User profile
     @PostMapping("/profile")
     public ResponseEntity<String> updateProfile( @Valid @RequestBody UserInfoDto userInfoDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
          User currentUser = userService.findById(userDetails.getId());
          userProfileService.saveOrUpdateProfile(userInfoDto, currentUser);
          return ResponseEntity.ok("Profile updated successfully!");
          
          
     }
    
    }
    


