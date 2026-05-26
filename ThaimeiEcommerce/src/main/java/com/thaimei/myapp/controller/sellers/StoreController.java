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
import com.thaimei.myapp.dto.sellersDto.StoresDto;
import com.thaimei.myapp.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;



@RestController
@RequestMapping("/sellers")
public class StoreController {
    private final StoreService storeService;
    public StoreController( StoreService storeService) {
        this.storeService = storeService;
    }
    @PostMapping("/addBusiness")
    public ResponseEntity<String> registerStore(@Valid @RequestBody RegisterStoreDto storeDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        storeService.saveStore(storeDto, user);
        return ResponseEntity.ok("Store registered successfully!"); 
    }

    @GetMapping("/getStoresForSeller")
    public ResponseEntity<List<StoresDto>> Stores (@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<StoresDto> stores=storeService.getStoresByUser(user);
        return ResponseEntity.ok(stores);
        


    }
    
}
