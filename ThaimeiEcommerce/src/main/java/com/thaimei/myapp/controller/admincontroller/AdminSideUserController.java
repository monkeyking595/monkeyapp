package com.thaimei.myapp.controller.admincontroller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.service.UserService;
import  com.thaimei.myapp.dto.adminDto.AdminUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;
import com.thaimei.myapp.enums.RoleEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RequestMapping("/admin/api")
@RestController
public class AdminSideUserController {
    private final UserService userService;
    public AdminSideUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/AllUsers")
    public ResponseEntity<Slice<AdminUserDto>> getAllCustomer(@RequestParam (defaultValue ="0")int page, @RequestParam(defaultValue="20")int size, RoleEnum role) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<AdminUserDto> users = userService.getUserByRole(RoleEnum.CUSTOMER, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllSellers")
    public ResponseEntity<Slice<AdminUserDto>> allSellersForAdmin(@RequestParam(defaultValue ="0") int page, @RequestParam(defaultValue = "20") int size) {
        //page always comes first in pageable
        Pageable pageable = PageRequest.of(page, size);
        Slice<AdminUserDto> users = userService.getUserByRole(RoleEnum.SELLER, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/searchSellers")
    public ResponseEntity<AdminUserDto> searchSeller(@RequestParam String email) {
        AdminUserDto seller = userService.searchByEmail(email);
        return ResponseEntity.ok(seller);
    }
    
}
