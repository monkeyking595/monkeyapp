package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.thaimei.myapp.enums.RoleEnum;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername (String username);
    Optional<User> findByUsername(String username);
    Page<User> findByRole(RoleEnum role, Pageable pageable);
    Optional<User> findByEmail(String email);
}
    

