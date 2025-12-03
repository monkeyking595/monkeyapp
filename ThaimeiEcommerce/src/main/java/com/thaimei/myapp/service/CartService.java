package com.thaimei.myapp.service;
import com.thaimei.myapp.model.Cart;
import com.thaimei.myapp.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.thaimei.myapp.dto.CartDto;
import com.thaimei.myapp.dto.AddItem;
import com.thaimei.myapp.model.cartItem;

@Service
public class CartService {
    private final  CartRepository cartRepository;
    private final ModelMapper modelMapper;
    public CartService(CartRepository cartRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    public void  addItemsToCart(AddItem addItem, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
        .orElse(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
        });
        

    }

    public  getCartByUserId(Long userId) {

    }
    
}
