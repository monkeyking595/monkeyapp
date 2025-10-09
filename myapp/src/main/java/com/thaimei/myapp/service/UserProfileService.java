package com.thaimei.myapp.service;
import com.thaimei.myapp.dto.UserInfoDto;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.repository.UserProfileRepo;
import  com.thaimei.myapp.model.UserprofileModel;
import org.modelmapper.ModelMapper;
import com.thaimei.myapp.model.User;
import java.util.Optional;


@Service
public class UserProfileService {
    private final UserProfileRepo userProfileRepo;
    private final ModelMapper modelMapper;
   
    public UserProfileService(UserProfileRepo userProfileRepo, ModelMapper modelMapper) {
        this.userProfileRepo = userProfileRepo;
        this.modelMapper = modelMapper;
        
    }
   
   
    public void  saveOrUpdateProfile(UserInfoDto userInfoDto,User currentUser) {
        Optional<UserprofileModel> existingProfile=userProfileRepo.findByUser(currentUser);
        UserprofileModel profile;
        if(existingProfile.isPresent()) {
            profile=existingProfile.get();
            modelMapper.map(userInfoDto, profile);
            
        }else {
            profile=modelMapper.map(userInfoDto, UserprofileModel.class);
            profile.setUser(currentUser);

        }
        userProfileRepo.save(profile);
    }   
     
    public UserInfoDto getprofileByUser(User currentUser) {
        Optional<UserprofileModel> existingProfile=userProfileRepo.findByUser(currentUser);
        if(existingProfile.isPresent()) {
            UserprofileModel profile=existingProfile.get();
            return modelMapper.map(profile, UserInfoDto.class);
        } else {
            //if no profile is not  found, this returns an empty dto object, to avoid null pointer exception
            return new UserInfoDto();
        }
    }
}
    
    

