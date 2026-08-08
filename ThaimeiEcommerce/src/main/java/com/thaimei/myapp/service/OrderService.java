package com.thaimei.myapp.service;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.model.Orders;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.OrderPlaceDto;
import com.thaimei.myapp.dto.OrderResponseDto;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.thaimei.myapp.dto.ItemRequestDto;

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


    public String checkout(OrderPlaceDto orderDto, User user) {
        //get a list of products since one order can have multiple products.
        List<Long> productId = orderDto.getOrderItems().stream()
        .map(ItemRequestDto::getProductId)
        .toList();

        
        //findById behaves just like findAll but it takes long as an argument.
        List<ProductsModel> products = productsRepo.findAllById(productId);
        
        //create a map of products with productId as key and ProductsModel as value for easy access.
        //behaves like a dictionary in python, where we can access the value by key.
        Map<Long, ProductsModel> productsMap = products.stream()
        .collect(Collectors.toMap(ProductsModel::getProductId, p -> p));

        //collector.groupingBy() --> a classifier method, it internal build a list and accumulate elements into the list (products in our case) base on the key (StoreModel), which we get from productsModel, since storeModel lives in ProductsModel (manyToOne relationship). 
        //why do this? since it's the business requirement, one order per Store, without grouping first we won't know which items belong to which store
        // we're grouping products by store, a list of products belonging to that store.
        Map<StoreModel, List<ItemRequestDto>> itemsByStore = orderDto.getOrderItems().stream()
        .collect(Collectors.groupingBy(item-> productsMap.get(item.getProductId()).getStoreModel()));

        //create a new list to hold all the orders, every iteration it's gonna build a new list for each order, one store = one order
        List<Orders> ordersToSave = new ArrayList<>();

        //here loop through the grouped store & item list and separate the Key from the value
        // entrySet(), a method of the Map, which is iterable, Map itself doesn't implement Iterable, it gives both the Key and Value, it returns a set of Map.Entry<>.
        // Map.Entry<>, this is a nested interface inside the Map, it represents one key - value pair, all the Key - pair inside the map is store as an Entry.
        for (Map.Entry<StoreModel, List<ItemRequestDto>> entry : itemsByStore.entrySet()) {
            //getKey(), a method of the Entry interface, it pulls the key from the current entry object.
            StoreModel store = entry.getKey();
            //getValue(), this pulls the value (a list) of the current Entry object.
            List<ItemRequestDto> storeItems = entry.getValue();

            // new Order object each iteration (one order per one Store)
            Orders order = new Orders();
            order.setUser(user);
            order.setStore(store);
            //get the status from the payment entity, if payment failed order fail
            order.setStatus(OrderStatusEnum.PENDING);

            // new OrderItems list for each order object (happens every new iteration)
            List<OrderItems> orderItemsList = new ArrayList<>();

            BigDecimal totalPrice = BigDecimal.ZERO;

            //loops through each orderItems (getValue()) for every new order order object.
            for (ItemRequestDto item : storeItems) {
                ProductsModel product = productsMap.get(item.getProductId());
                if (product == null) {
                    throw new ResourceNotFoundException( "product not found" + item.getProductId());
                }

                OrderItems orderItems = new OrderItems();
                orderItems.setQuantity(item.getQuantity());
                orderItems.setProduct(product);
                orderItems.setPriceAtPurchase(product.getPrice());
                orderItems.setProductNameAtPurchase(product.getName());
                orderItems.setOrders(order);

                orderItemsList.add(orderItems);

                //accumulates the total price of each iteration (totalPrice+=)
                totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            order.setOrderItems(orderItemsList);
            order.setTotalPrice(totalPrice);
            //add(), appends the order object into the empty list ordersToSave().
            ordersToSave.add(order);
        }
        // saveAll(), save  a list of orders 
        orderRepo.saveAll(ordersToSave);

        //find the grand total of all the orders.
        BigDecimal grandTotal = ordersToSave.stream()
        .map(Orders::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

        //converts the grand total into paise, since Stripe requires the amount to be in the smallest currency unit (paise for INR).
        //1 rupee = 100 paise
        //longValue(), a BigDecimal's method to convert the BigDecimal into a long, since Stripe requires the amount to be in long.
        long amountInPaise = grandTotal.multiply(BigDecimal.valueOf(100)).longValue();

        //collect the ids of all the orders so that webhook can find them later.
        String orderIds = ordersToSave.stream()
        //converts each ids into string, since these values are to PaymentIntent Stripe's metadata field and it only accepts Strings.
        .map(o -> o.getId().toString())
        //join all the ids into a single string separated by comma
        // why do this? because Stripe imposes limits (roughly 50 keys max, each key/value has a length cap) it's meant for small amounts of simple, flat data, not for storing structured/nested data.
        //and there are several other reasons too, joining them into single is just a better alternative.
        .collect(Collectors.joining(","));

        try {
        // PaymentIntentCreateParams, a class from Stripe's java SDK, representing all the parameters when creating a paymentIntent.
        //builder(), a static method of the PaymentIntentCreateParams class, it returns a new instance of the builder class, which is used to build the PaymentIntentCreateParams object.
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        .setAmount(amountInPaise)
        //"inr" here has to be a value Stripe recognizes — specifically, a valid ISO 4217 currency code, lowercase, from a fixed list Stripe supports ("inr", "usd", "eur", "gbp", etc.).
        .setCurrency("inr")
        //metadata internally is a Map, and putMetadata adds one key-value pair at a time (like calling .put() on a Map directly), rather than replacing the whole map at once.
        .putMetadata("orderIds", orderIds)
        //the data in the metadata will be used to link back the payment to the userand the order, so that when the webhook is called, we can find the user and the order and update the status accordingly.
        .putMetadata("userId", user.getId().toString())
        //build(), this is the final step in the builder chain -- it's the method thay actually takes everything you've set on the Builder and uses those values to construct the real, final PaymentIntentCreateParams object.
        .build();

        //create the actual paymentIntent object using the params we just built, this is the object that will be sent to Stripe to create a paymentIntent.
        //create(), a static method of the PaymentIntent class, it takes the params we built and sends a request to Stripe's API to create a new PaymentIntent with those parameters.
        PaymentIntent intent  = PaymentIntent.create(params);
        return intent.getClientSecret();
        } catch (StripeException e) {
            //if payment creation fails, don't roll back the whole order but keep them as failed, so that the user can try again later, and we can keep track of the failed orders for analysis.
            ordersToSave.forEach(o-> o.setStatus(OrderStatusEnum.FAILED));
            orderRepo.saveAll(ordersToSave);
            throw new AppException("Failed to create payment for order", 502);
        }
    }


    public List<OrderResponseDto> getOrdersByUserId(User user) {
        List<Orders> orders=orderRepo.findByUser_Id(user.getId());
        return orders.stream()
        .map(order->modelMapper.map(order,OrderResponseDto.class))
        .toList();
    }
    
    public Slice <AdminOrderDto> getAdminOrders( @NonNull Pageable pageable) {
        Page <Orders> orders=orderRepo.findAll(pageable);
        return orders
        // here block lambda is used to map the orders entity to adminorderDto.
        // the block lambda uses "{}" to define the body of the lambda, 
        // the only difference between block and expression is that blocks deals with multiple statements 
        // it uses the return keyword to return the explicit object we want, whereas in block we don't need it since it has only one statement.
        // in block we need to create the object explicitly "dto" in my case.
        // we do this since we use modelmapper which map fields which matches the other and here we're adding fields which is not exactly in the orders entity. 
        .map(order-> { AdminOrderDto dto = modelMapper.map(order,AdminOrderDto.class);
            dto.setUserId(order.getUser().getId());
            dto.setUserName(order.getUser().getUsername());
            dto.setStoreId(order.getStore().getStoreId());
            dto.setStoreName(order.getStore().getStoreName());
            return dto;
        });
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

        Slice<Orders> orders = orderRepo.findByStore_StoreId(storeId, pageable);

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
