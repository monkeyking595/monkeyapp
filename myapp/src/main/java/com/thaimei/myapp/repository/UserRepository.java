package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername (String username);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}
    

