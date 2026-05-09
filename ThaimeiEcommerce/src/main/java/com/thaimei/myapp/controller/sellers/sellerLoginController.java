package com.thaimei.myapp.controller.sellers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@RequestMapping("seller")
@RestController
public class sellerLoginController {
    
    @GetMapping("/login")
    public ResponseEntity<String> sellerLogin() {

    }
    @PostMapping("/sellerLogin")
    public ResponseEntity<String> sellerLoginPost() {}
    
}
