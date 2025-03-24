package com.edu.qlda.repository;

import com.edu.qlda.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    @Query(value = "select  role_id, name  from role", nativeQuery = true)
    List<Role> findAllRole();
}
