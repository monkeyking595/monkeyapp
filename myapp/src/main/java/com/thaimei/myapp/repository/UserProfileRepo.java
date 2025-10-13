package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.UserprofileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.thaimei.myapp.model.User;


public interface UserProfileRepo extends JpaRepository<UserprofileModel, Long> {
    Optional<UserprofileModel> findByUser(User currentUser);
}
