package com.edu.qlda.controller;

import com.edu.qlda.dto.*;

import com.edu.qlda.entity.Employee;
import com.edu.qlda.entity.Position;

import com.edu.qlda.entity.Role;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.EmployeeService;
import com.edu.qlda.service.PositionService;
import com.edu.qlda.service.RoleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class EmployeeController {
    private static final String ACTIONSUCESS = "Thành công";
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final RoleService roleService;

    public EmployeeController(EmployeeService employeeService, PositionService positionService, RoleService roleService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.roleService = roleService;
    }

    //Lấy ds cá nhân
    @GetMapping("/employee")
    public List<EmployeelistDto> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeService.findAllEmployee(pageable);
    }

    //Lấy ds cá nhân
    @GetMapping("/position")
    public List<Position> getAllPosition() {
        return positionService.listPosition();
    }

    @GetMapping("/role")
    public List<Role> getAllRole() {
        return roleService.listRole();
    }

    // Lấy ds cá nhân theo id
    @GetMapping("/employeebyId")
    public EmployeelistDto employeeById(int id) {
        return employeeService.findEmployeeId(id);
    }

    //Search cá nhân
    @GetMapping("/employee/search")
    public List<EmployeelistDto> search(@RequestParam(name = "name", required = false) String name) {
        return employeeService.searchEmployee(name);
    }

    @PostMapping("searchadvance")
    public List<EmployeelistDto> searchadvance(@RequestBody EmployeesearchDto employeesearchDto) {
        return employeeService.searchadvance(employeesearchDto
        );
    }

    // Thêm mới cá nhân
    @PostMapping("/addemployee")
    public ResponseEntity<Messageresponse<Void>> createemployee(@RequestBody EmployeecreateDto employeeDto) {
        try {
            employeeService.createemployee(employeeDto);
            Messageresponse<Void> response = new Messageresponse<>(200, ACTIONSUCESS);
            return ResponseEntity.ok(response);
        }
        // Xử lý ngoại lệ các dữ lệu
        catch (RuntimeException e) {
            Messageresponse<Void> response = new Messageresponse<>(409, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    //Cập nật cá nhân
    @PutMapping("/updateemployee")
    public ResponseEntity<Messageresponse<Void>> updateemployee(@RequestBody EmployeeEditDto employeeEditDto) {
        try {

            employeeService.updateemployee(employeeEditDto);
            Messageresponse<Void> response = new Messageresponse<>(200, ACTIONSUCESS);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Messageresponse<Void> response = new Messageresponse<>(409, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

    }

    //Xóa cá nhân
    @DeleteMapping("delete-employee/{id}")
    public void deleteemployee(@PathVariable Integer id) {
        employeeService.deleteemployee(id);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Messageresponse<Void>> login(@RequestBody Loginrequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        Employee employee = employeeService.isUserValid(email, password);
        if (employee != null) {
            // Tạo một Response object
            Messageresponse response = new Messageresponse(
                    200, ACTIONSUCESS,
                    new Employee[]{employee}
            );
            // Trả về ResponseEntity với mã trạng thái HTTP là OK (200)
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            // Tạo một Response object
            Messageresponse response = new Messageresponse(400, "Tài khoản không tồn tại", "");
            // Trả về ResponseEntity với mã trạng thái HTTP là OK (200)
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
