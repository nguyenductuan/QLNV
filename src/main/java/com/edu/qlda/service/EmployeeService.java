package com.edu.qlda.service;

import com.edu.qlda.dto.*;
import com.edu.qlda.entity.Employee;
import com.edu.qlda.exception.EmployeeAlreadyExistsException;
import com.edu.qlda.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import static java.util.regex.Pattern.matches;

@Service
public class EmployeeService {

    private final  EmployeeRepository employeeRepository;
 public   EmployeeService(EmployeeRepository employeeRepository){
     this.employeeRepository=employeeRepository;
 }
    public List<EmployeelistDto> findAllEmployee(Pageable pageable) {
        return employeeRepository.findAllEmployee(pageable);
    }
    public EmployeelistDto findEmployeeId(int id) {
        return employeeRepository.findByID(id);
    }
    public void deleteemployee(Integer id) {
        employeeRepository.deleteemployee(id);
    }
    public Employee isUserValid(String email, String password) {
        Employee user = getAccountByname(email);
        if (user != null && matches(password, user.getPassword()) ) {
            return user;
        }
        return null;
    }
    public boolean isEmployeeEmailExist(String email) {
        return employeeRepository.existsByEmail(email);
    }
    public void  createemployee(EmployeecreateDto request) {
        if (isEmployeeEmailExist(request.getEmail())) {
            throw new EmployeeAlreadyExistsException("Nhân viên đã tồn tại trong hệ thống");
        }
        LocalDate createdate = LocalDate.now();
        LocalDate updatedate = null;
         employeeRepository.createEmployee(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                request.getStatus(),
                request.getRole(),
                request.getPosition(),
                request.getAddress(),
                createdate,
                updatedate,
                request.getPassword(),
                request.getGender(),
                request.getBirthday());
    }
    public void updateemployee(EmployeeEditDto request) {
        LocalDate updatedate = LocalDate.now();

        employeeRepository.editEmployee(
                request.getName(),
                Integer.parseInt(request.getPosition()),
                Integer.parseInt(request.getRole()),
                request.getPassword(),
                request.getAddress(),
                request.getBirthday(),
                request.getEmail(),
                request.getGender(),
                request.getPhone(),
                request.getStatus(),
                updatedate,
                request.getEmployeeId()
        );
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
}