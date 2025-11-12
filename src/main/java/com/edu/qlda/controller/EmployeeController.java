package com.edu.qlda.controller;

import com.edu.qlda.dto.*;
import com.edu.qlda.entity.Employee;
import com.edu.qlda.entity.Position;
import com.edu.qlda.entity.Role;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.InputStreamResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("http://localhost:4200")
@Tag(name = "Employee Controller", description = "API quản lý nhân viên, chức vụ, quyền hạn")
public class EmployeeController {
    private static final String ACTIONSUCESS = "Thành công";
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final RoleService roleService;
    private final ExcelService excelService;
    private final ImportService importService;

    public EmployeeController(EmployeeService employeeService,
                              PositionService positionService,
                              RoleService roleService,
                              ExcelService excelService,
                              ImportService importService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.roleService = roleService;
        this.excelService = excelService;
        this.importService = importService;
    }

    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    @GetMapping("/employee")
    public List<EmployeelistDto> getAllEmployees() {
        return employeeService.findAllEmployee();
    }

    @Operation(summary = "Lấy danh sách tất cả chức vụ")
    @GetMapping("/position")
    public List<Position> getAllPositions() {
        return positionService.listPosition();
    }

    @Operation(summary = "Lấy danh sách tất cả quyền hạn")
    @GetMapping("/role")
    public List<Role> getAllRoles() {
        return roleService.listRole();
    }

    // Lấy ds cá nhân theo id
    @GetMapping("/employeebyId")
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public EmployeelistDto employeeById(int id) {
        return employeeService.findEmployeeId(id);
    }

    //Search cá nhân
    @GetMapping("/employee/search")
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public List<EmployeelistDto> search(@RequestParam(name = "name", required = false) String name) {
        return employeeService.searchEmployee(name);
    }

    @PostMapping("searchadvance")
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public List<EmployeelistDto> searchadvance(@RequestBody EmployeesearchDto employeesearchDto) {
        return employeeService.searchadvance(employeesearchDto
        );
    }

    // Thêm mới cá nhân
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    @PostMapping("/addemployee")
    public ResponseEntity<Messageresponse<Employee>> createemployee(@Valid @RequestBody Employee employeeDto,
                                                                    BindingResult bindingResult) {
        try {
            ResponseEntity<Messageresponse<Employee>> errorResponse = handleValidationErrors(bindingResult);
            if (errorResponse != null) return errorResponse;
            employeeService.createemployee(employeeDto);
            return ResponseEntity.ok(new Messageresponse<>(200, "Thêm nhân viên thành công"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    //Cập nật cá nhân
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    @PutMapping("/updateemployee/{id}")
    public ResponseEntity<Messageresponse<Employee>> updateemployee(@Valid @RequestBody Employee employeeDto,
                                                                    BindingResult bindingResult, @PathVariable Integer id) {
        try {
            ResponseEntity<Messageresponse<Employee>> errorResponse = handleValidationErrors(bindingResult);
            if (errorResponse != null) return errorResponse;
            employeeService.updateemployee(employeeDto, id);
            Messageresponse<Employee> response = new Messageresponse<>(200, "Cập nhật nhân viên thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    //Xóa cá nhân
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    @DeleteMapping("delete-employee/{id}")
    public void deleteemployee(@PathVariable Integer id) {
        employeeService.deleteemployee(id);
    }


    //Xuất Excel
    @GetMapping("employee/export-excel")
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
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

    // Xóa nhiều cá nhân
    @PostMapping("delete-employees")
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteEmployees(@RequestBody List<Integer> ids) {

        try {
            // Kiểm tra danh sách có rỗng không
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(
                        new Messageresponse<>(404, "Danh sách nhân viên cần xóa không được để trống"));
            }
            // danh sách mã giảm giá cần xóa
            List<Integer> notFoundIds = employeeService.deleteEmployees(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(
                        new Messageresponse<>(200, "Danh sách nhân viên đã được xóa thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(
                        new Messageresponse<>(404, "Một số nhân viên không tồn tại", notFoundIds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new Messageresponse<>(500, "Lỗi hệ thống:" + e.getMessage()));
        }
    }

    // Phương thức xử lý chung
    private ResponseEntity<Messageresponse<Employee>> handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(new Messageresponse<>(201, message));
        }
        return null;
    }

    private ResponseEntity<Messageresponse<Employee>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Messageresponse<>(409, e.getMessage()));
    }
  
    @PostMapping("/import")

        public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Vui lòng chọn một file để upload.");
            }

            try {
                // Xử lý file (ví dụ CSV/Excel)

                String message = file.getOriginalFilename() + " được tải lên thành công.";

                // Gọi service xử lý nội dung file
               importService.processFile(file);

                return ResponseEntity.ok(message);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xử lý file: " + e.getMessage());
            }
        }


}

