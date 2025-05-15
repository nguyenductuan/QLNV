package com.edu.qlda.service;

import com.edu.qlda.dto.EmployeelistDto;
import com.edu.qlda.dto.EmployeesearchDto;
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

    public List<EmployeelistDto> getAllEmployees() {
        return employeeRepository.findAllEmployee();
    }

    public EmployeelistDto getEmployeeById(int id) {
        return employeeRepository.findByID(id);
    }

    public List<Employee> getAllEmployeeEntities() {
        return employeeRepository.findAll();
    }

    public List<EmployeelistDto> searchEmployeeByName(String name) {
        return employeeRepository.searchemployee(name);
    }

    public List<EmployeelistDto> advancedSearchEmployees(EmployeesearchDto search) {
        return employeeRepository.searchEmployeesWithDateRange(search);
    }

    public Employee getAccountByEmail(String email) {
        return employeeRepository.findByName(email);
    }

    public Employee validateUser(String email, String password) {
        Employee user = getAccountByEmail(email);
        if (user != null && matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean isEmployeeEmailExist(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public void createEmployee(@Valid Employee request) {
        if (isEmployeeEmailExist(request.getEmail())) {
            throw new ValidationException("Nhân viên đã tồn tại trong hệ thống");
        }
        request.setCreatedate(LocalDate.now());
        employeeRepository.save(request);
    }

    public void updateEmployee(Employee request, Integer id) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Nhân viên không tồn tại trong hệ thống"));

        boolean isEmailChanged = !existingEmployee.getEmail().equals(request.getEmail());
        if (isEmailChanged && isEmployeeEmailExist(request.getEmail())) {
            throw new ValidationException("Email đã được sử dụng bởi nhân viên khác");
        }

        existingEmployee.setName(request.getName());
        existingEmployee.setPhone(request.getPhone());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setAddress(request.getAddress());
        existingEmployee.setBirthday(request.getBirthday());
        existingEmployee.setGender(request.getGender());
        existingEmployee.setPosition(request.getPosition());
        existingEmployee.setRole(request.getRole());
        existingEmployee.setUpdatedate(LocalDate.now());

        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Integer id) {
        employeeRepository.deleteemployee(id);
    }

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

    //login
    public boolean validateUsers(String username, String password) {
        // Tạm thời hardcoded. Thực tế nên kiểm tra với DB
        return "admin".equals(username) && "123456".equals(password);
    }
}
