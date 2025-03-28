package com.edu.qlda.repository;
import com.edu.qlda.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
