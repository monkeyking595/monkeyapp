package com.thaimei.myapp.controller.sellers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.thaimei.myapp.dto.sellersDto.RegisterStoreDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.service.StoreService;
import com.thaimei.myapp.model.User;



@RestController
@RequestMapping("/seller")
public class StoreController {
    private final StoreService brandService;
    public StoreController(StoreService brandService) {
        this.brandService = brandService;
    }
    @PostMapping("/addBusiness")
    public ResponseEntity<String> registerStore(@Valid @RequestBody RegisterStoreDto storedDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        brandService.saveStore(storedDto, user);
        return ResponseEntity.ok("Store registered successfully!"); 
    }
    
}
