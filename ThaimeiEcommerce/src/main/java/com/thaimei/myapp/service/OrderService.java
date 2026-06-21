package com.thaimei.myapp.service;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.model.Orders;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.OrderPlaceDto;
import com.thaimei.myapp.dto.OrderResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import com.thaimei.myapp.dto.adminDto.AdminOrderDto;
import com.thaimei.myapp.model.ProductsModel;
import com.thaimei.myapp.repository.ProductsRepo;
import com.thaimei.myapp.model.User;
import java.math.BigDecimal;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
  
    private final ProductsRepo productsRepo;
    public OrderService(OrderRepo orderRepo, ModelMapper modelMapper, ProductsRepo productsRepo) {
        this.orderRepo=orderRepo;
        this.modelMapper=modelMapper;
        this.productsRepo=productsRepo;
    }
    public void saveOrders(OrderPlaceDto orderDto, User user) {
        ProductsModel product = productsRepo.findById(orderDto.getProductId())
        .orElseThrow(()-> new ResourceNotFoundException ("product not found"));
        
        Orders order = new Orders();
        order.setUser(user);
        //store the product_id as a reference(as foreign-key) to the product entity
        order.setProduct(product);
        order.setQuantity(orderDto.getQuantity());
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf((orderDto.getQuantity()))));
        order.setOrderStatus(OrderStatusEnum.PENDING);
        orderRepo.save(order);
    }
    public List<OrderResponseDto> getOrdersByUserId(User user) {
        List<Orders> orders=orderRepo.findByUserId(user.getId());
        return orders.stream()
        .map(order->modelMapper.map(order,OrderResponseDto.class))
        .toList();
    }
    
    public Slice <AdminOrderDto> getAdminOrders( @NonNull Pageable pageable) {
        Page <Orders> orders=orderRepo.findAll(pageable);
        return orders
        .map(order->modelMapper.map(order,AdminOrderDto.class));
    } 
    //logging will be integrated later...  
}
