package com.example.mappingvaluetypes.repository;

import com.example.mappingvaluetypes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
