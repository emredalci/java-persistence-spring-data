package com.example.onetomanyembeddablejointable.repository;


import com.example.onetomanyembeddablejointable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
