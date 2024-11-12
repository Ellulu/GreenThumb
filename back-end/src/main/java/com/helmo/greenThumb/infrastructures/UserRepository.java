package com.helmo.greenThumb.infrastructures;


import com.helmo.greenThumb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
