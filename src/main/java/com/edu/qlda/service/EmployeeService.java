package com.edu.qlda.service;

import com.edu.qlda.dto.*;
import com.edu.qlda.entity.Employee;
import com.edu.qlda.exception.ValidationException;

import com.edu.qlda.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.regex.Pattern.matches;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeelistDto> findAllEmployee() {
        return employeeRepository.findAllEmployee();
    }

    public EmployeelistDto findEmployeeId(int id) {
        return employeeRepository.findByID(id);
    }

    public void deleteemployee(Integer id) {
        employeeRepository.deleteemployee(id);
    }

    public Employee isUserValid(String email, String password) {
        Employee user = getAccountByname(email);
        if (user != null && matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean isEmployeeEmailExist(String email) {
        return employeeRepository.existsByEmail(email);
    }
    public void createemployee(@Valid Employee request) {
        if (isEmployeeEmailExist(request.getEmail())) {
            throw new ValidationException("Nhân viên đã tồn tại trong hệ thống");
        }
        request.setCreatedate(LocalDate.now());
        employeeRepository.save(request);
    }
    public void updateemployee(Employee request, Integer id) {
        LocalDate updatedate = LocalDate.now();
        Optional<Employee> employee = employeeRepository.findById(id);
        if (isEmployeeEmailExist(request.getEmail())) {
            throw new ValidationException("Nhân viên đã tồn tại trong hệ thống");
        }
        Employee existingEmployee;
        if (employee.isPresent()) {
            existingEmployee = employee.get();
            // Tiếp tục xử lý product
        } else {
            throw new ValidationException("Nhân viên không tồn tại trong hệ thống");
        }
        existingEmployee.setName(request.getName());
        existingEmployee.setPhone(request.getPhone());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setAddress(request.getAddress());
        existingEmployee.setBirthday(request.getBirthday());
        existingEmployee.setGender(request.getGender());
        existingEmployee.setPosition(request.getPosition());
        existingEmployee.setRole(request.getRole());
        existingEmployee.setUpdatedate(updatedate);
        employeeRepository.save(existingEmployee);
    }

    public List<EmployeelistDto> searchEmployee(String name) {

        return employeeRepository.searchemployee(name);
    }

    public Employee getAccountByname(String email) {
        return employeeRepository.findByName(email);
    }

    public List<EmployeelistDto> searchadvance(EmployeesearchDto search) {
        return employeeRepository.searchEmployeesWithDateRange(search);
    }
    public List<Employee> listemployee() {
        return employeeRepository.findAll();
    }
// Xóa nhiều cá nhân
    public List<Integer> deleteEmployees(List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        for (Integer id : ids) {
            if (employeeRepository.existsById(id)) {
                employeeRepository.deleteById(id);
            } else {
                notFoundIds.add(id);
            }
        }
        return notFoundIds;
    }

}
