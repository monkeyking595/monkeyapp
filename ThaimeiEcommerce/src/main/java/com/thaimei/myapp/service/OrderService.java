package com.thaimei.myapp.service;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.model.Orders;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.OrderDto;
import org.modelmapper.ModelMapper;
import java.util.Objects;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    public OrderService(OrderRepo orderRepo, ModelMapper modelMapper) {
        this.orderRepo=orderRepo;
        this.modelMapper=modelMapper;
    }
    public Orders saveOrders(OrderDto orderDto) {
        Orders order = Objects.requireNonNull (modelMapper.map(orderDto, Orders.class), "Order mapping failed");
        return orderRepo.save(order);
    }
    public List<OrderDto> getOrdersByUserId(long userId) {
        List<Orders> orders=orderRepo.findByUserId(userId);
        return orders.stream()
        .map(order->modelMapper.map(order,OrderDto.class))
        .toList();

    }

    
}
