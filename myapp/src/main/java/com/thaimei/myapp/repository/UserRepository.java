package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername (String username);
    User findByUsername(String username);
}
    

