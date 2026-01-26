package com.thaimei.myapp.service;
import com.thaimei.myapp.model.Cart;
import com.thaimei.myapp.repository.CartRepository;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.CartDto;
import com.thaimei.myapp.dto.AddItem;
import com.thaimei.myapp.model.CartItem;
import com.thaimei.myapp.model.ProductsModel;
import com.thaimei.myapp.repository.CartItemRepository;
import java.util.Optional;
import com.thaimei.myapp.repository.ProductsRepo;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;

@Service
public class CartService {
    private final  CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductsRepo productsRepo;
    private final ModelMapper modelMapper;
    public CartService(CartRepository cartRepository, ModelMapper modelMapper, CartItemRepository cartItemRepository, ProductsRepo productsRepo) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productsRepo = productsRepo;
        this.modelMapper = modelMapper;
    }

    public void  addItemsToCart(AddItem addItem, Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
        .orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        
        ProductsModel product = productsRepo.findById(addItem.getProductId())
        .orElseThrow(() -> new RuntimeException("product not found or out of stock"));

        //check if item already exists in cart if true merge quantities.
        Optional<CartItem> existingItem= cartItemRepository.findByCartIdAndProductId(cart.getCartId(), addItem.getProductId());
        if(existingItem.isPresent()) {
            //extract the existing item and update quantity.
            CartItem item =existingItem.get();
            int newQuantity = item.getQuantity() + addItem.getQuantity();
            item.setQuantity(newQuantity);
            item.setTotalPrice(product.getPrice() * newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(addItem.getProductId());
            newItem.setQuantity(addItem.getQuantity());
            newItem.setPrice(product.getPrice());
            newItem.setProductName(product.getName());
            newItem.setImageURL(product.getImageURL());
            newItem.setDescription(product.getDescription());
            newItem.setTotalPrice(product.getPrice() * addItem.getQuantity());
            cartItemRepository.save(newItem);
        }

    }

   public Optional<CartDto> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
        .map(cart -> modelMapper.map(cart, CartDto.class));
   }
}
