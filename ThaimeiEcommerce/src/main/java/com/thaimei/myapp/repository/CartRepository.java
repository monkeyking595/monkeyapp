package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.Cart;
import com.thaimei.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}