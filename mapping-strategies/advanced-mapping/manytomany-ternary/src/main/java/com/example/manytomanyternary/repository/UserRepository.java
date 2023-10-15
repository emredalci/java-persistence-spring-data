package com.example.manytomanyternary.repository;

import com.example.manytomanyternary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
