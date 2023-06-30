package com.example.manytomanybidirectional.repository;

import com.example.manytomanybidirectional.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
