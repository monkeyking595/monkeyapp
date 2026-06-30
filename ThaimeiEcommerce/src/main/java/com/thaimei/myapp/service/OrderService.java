package com.thaimei.myapp.service;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.model.Orders;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.OrderPlaceDto;
import com.thaimei.myapp.dto.OrderResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import com.thaimei.myapp.dto.adminDto.AdminOrderDto;
import com.thaimei.myapp.dto.sellersDto.SellerOrdersResponse;
import com.thaimei.myapp.model.ProductsModel;
import com.thaimei.myapp.repository.ProductsRepo;
import com.thaimei.myapp.model.User;
import java.math.BigDecimal;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.error.AppException;
import com.thaimei.myapp.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import com.thaimei.myapp.model.OrderItems;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.repository.StoreRepo;
import com.thaimei.myapp.repository.UserRepository;
@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final StoreRepo storeRepo;
    private final UserRepository userRepository;
    private final ProductsRepo productsRepo;
    public OrderService(OrderRepo orderRepo, ModelMapper modelMapper, ProductsRepo productsRepo, StoreRepo storeRepo, UserRepository userRepository) {
        this.orderRepo=orderRepo;
        this.modelMapper=modelMapper;
        this.productsRepo=productsRepo;
        this.storeRepo = storeRepo;
        this.userRepository = userRepository;
    }
    public void saveOrders(OrderPlaceDto orderDto, User user) {
        ProductsModel product = productsRepo.findById(orderDto.getProductId())
        .orElseThrow(()-> new ResourceNotFoundException ("product not found"));
        
        Orders order = new Orders();
        order.setUser(user);
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf((orderDto.getQuantity()))));
        order.setStatus(OrderStatusEnum.PENDING);
        //one order per store, and each order holds a list of orderItems which has a relationship with product.
        //when user buy products from multiple store then the number of order increases else the number of order is constant for that user.
        order.setStore(product.getStoreModel());

        OrderItems orderItems = new OrderItems();
        orderItems.setQuantity(orderDto.getQuantity());
        //store the product_id as a reference(as foreign-key) to the product entity
        orderItems.setProduct(product);
        //store a snapshot of the price at the time of ordering
        orderItems.setPriceAtPurchase(product.getPrice());
        orderItems.setProductNameAtPurchase(product.getName());
        orderItems.setOrders(order);
        //returns the orderItems as list since the order entity holds a list of OrderItems
        order.setOrderItems(List.of(orderItems));
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

    public Slice<SellerOrdersResponse> getOrdersBySeller(User user, Pageable pageable) {
        //findByStore_User, the underscore in this method is spring data Jpa's property traversal syntax for derived query methods, it tells spring data to navigate through
        //nested object's field, when the field name itself could be ambiguous.
        //it means from Orders, go to the store field then from that store, go to its user field.
        //it tells where one property ends and one starts.
        Slice<Orders> orders = orderRepo.findByStore_User(user, pageable);
        return orders 
        .map(order -> modelMapper.map(order, SellerOrdersResponse.class));
    }
    
    public Slice<SellerOrdersResponse> getOrderByStore (Long sellerId, Pageable pageable, long storeId) {
        StoreModel store = storeRepo.findById(storeId) 
        .orElseThrow(() -> new ResourceNotFoundException("store not found"));

        if(!store.getUser().getId().equals(sellerId)) {
            throw new AppException("You do not  own this store",  403);
        }

        Slice<Orders> orders = orderRepo.findByStoreId(storeId, pageable);

        return orders
        .map(order -> modelMapper.map(order, SellerOrdersResponse.class));
    }

    public Slice<AdminOrderDto> getOrderForAdminBySeller(long sellerId, Pageable pageable) {
        User user = userRepository.findById(sellerId)
        .orElseThrow(()-> new ResourceNotFoundException("Seller not found"));

        Slice<Orders> orders = orderRepo.findByStore_User(user, pageable);

        return orders
        .map(order-> modelMapper.map(order, AdminOrderDto.class));
    }
}
