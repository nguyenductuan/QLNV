package com.edu.qlda.controller;
import com.edu.qlda.dto.*;
import com.edu.qlda.entity.Employee;
import com.edu.qlda.entity.Position;
import com.edu.qlda.entity.Role;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.EmployeeService;
import com.edu.qlda.service.ExcelService;
import com.edu.qlda.service.PositionService;
import com.edu.qlda.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class EmployeeController {
    private static final String ACTIONSUCESS = "Thành công";
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final RoleService roleService;
    private final ExcelService excelService;

    public EmployeeController(EmployeeService employeeService,
                              PositionService positionService,
                              RoleService roleService,
                              ExcelService excelService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.roleService = roleService;
        this.excelService = excelService;
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
    public ResponseEntity<Messageresponse<Employee>> createemployee(@Valid @RequestBody Employee employeeDto, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                String message = (fieldError != null) ? fieldError.getDefaultMessage() : "";

                Messageresponse<Employee> response = new Messageresponse<>(201 ,message );
            return ResponseEntity.ok(response);
        }
            employeeService.createemployee(employeeDto);
            Messageresponse<Employee> response = new Messageresponse<>(200, ACTIONSUCESS);
            return ResponseEntity.ok(response);
        }
        // Xử lý ngoại lệ các dữ lệu
        catch (Exception e) {
            Messageresponse<Employee> response = new Messageresponse<>(409, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    //Cập nật cá nhân
    @PutMapping("/updateemployee/{id}")
    public ResponseEntity<Messageresponse<Void>> updateemployee(@RequestBody Employee employeeEditDto, @PathVariable Integer id) {
        try {
            employeeService.updateemployee(employeeEditDto,id);
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

    @GetMapping("/export-excel")
    public ResponseEntity<InputStreamResource> exportEmployeesToExcel() {
        List<Employee> employees = employeeService.listemployee();
        ByteArrayInputStream in = excelService.generateExcel(employees);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employees.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));

    }
}
