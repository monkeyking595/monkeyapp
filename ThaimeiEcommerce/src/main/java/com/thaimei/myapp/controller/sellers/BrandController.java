package com.thaimei.myapp.controller.sellers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.thaimei.myapp.dto.sellersDto.BrandDto;


@RestController
@RequestMapping("/seller")
public class BrandController {
    @PostMapping("/addBusiness")
    public ResponseEntity<String> registerBusiness(@Valid @RequestBody BrandDto brandDto) {
        
    }
    
}
