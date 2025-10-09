package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.thaimei.myapp.dto.UserInfoDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thaimei.myapp.security.CustomuserDetails;
import com.thaimei.myapp.service.UserProfileService;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.service.UserService;



@RequestMapping
@Controller
public class UserProfileController {
     
     private final UserProfileService userProfileService;
     private final UserService userService;
     public UserProfileController(UserProfileService userProfileService, UserService userService) {
          this.userProfileService = userProfileService;
          this.userService = userService;
     }
     @GetMapping("/profile")
    public String profile() {
    return "UserProfile";
    }

   
    
    //fetch user info from the DB
    @ResponseBody
    @GetMapping("/profile/PersonalInfo")
    public ResponseEntity<UserInfoDto> getProfile(@AuthenticationPrincipal CustomuserDetails userDetails)  {
            User currentUser = userService.findByUserId(userDetails.getId());
            UserInfoDto userInfoDto = userProfileService.getprofileByUser(currentUser);
            return ResponseEntity.ok(userInfoDto);
    }
    //Save or update User profile
     @PostMapping("/profile")
     @ResponseBody
     public ResponseEntity<String> updateProfile( @RequestBody UserInfoDto userInfoDto, @AuthenticationPrincipal CustomuserDetails userDetails) {
          User currentUser = userService.findByUserId(userDetails.getId());
          userProfileService.saveOrUpdateProfile(userInfoDto, currentUser);
          return ResponseEntity.ok("Profile updated successfully!");
          
          
     }
    
    }
    


