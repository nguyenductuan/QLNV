package com.edu.qlda.service;



import com.edu.qlda.entity.Role;

import com.edu.qlda.repository.RoleRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {


    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> listRole(){

        return roleRepository.findAllRole();
    }
}
