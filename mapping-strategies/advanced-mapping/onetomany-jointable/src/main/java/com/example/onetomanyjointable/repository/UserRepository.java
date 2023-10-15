package com.example.onetomanyjointable.repository;

import com.example.onetomanyjointable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
