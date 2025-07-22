package com.edu.qlda.controller;

import com.edu.qlda.dto.*;
import com.edu.qlda.entity.Employee;
import com.edu.qlda.entity.Position;
import com.edu.qlda.entity.Role;
import com.edu.qlda.playload.response.response.Messageresponse;
import com.edu.qlda.service.EmployeeService;
import com.edu.qlda.service.ExcelService;
import com.edu.qlda.service.PositionService;
import com.edu.qlda.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:4200")
@Tag(name = "Employee Controller", description = "API quản lý nhân viên, chức vụ, quyền hạn")
public class EmployeeController {

    private static final String ACTION_SUCCESS = "Thành công";

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

    @GetMapping("/employee")
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public List<EmployeelistDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/position")
    @Operation(summary = "Lấy danh sách tất cả chức vụ")
    public List<Position> getAllPositions() {
        return positionService.listPositions();
    }

    @GetMapping("/role")
    @Operation(summary = "Lấy danh sách tất cả quyền hạn")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/employee/{id}")
    @Operation(summary = "Lấy thông tin nhân viên theo ID")
    public EmployeelistDto getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/employee/search")
    @Operation(summary = "Tìm kiếm nhân viên theo tên")
    public List<EmployeelistDto> searchEmployees(@RequestParam(name = "name", required = false) String name) {
        return employeeService.searchEmployeeByName(name);
    }

    @PostMapping("/employee/search-advanced")
    @Operation(summary = "Tìm kiếm nâng cao nhân viên")
    public List<EmployeelistDto> searchEmployeesAdvanced(@RequestBody EmployeesearchDto dto) {
        return employeeService.advancedSearchEmployees(dto);
    }
    @PostMapping("/employee")
    @Operation(summary = "Thêm mới nhân viên")
    public ResponseEntity<Messageresponse<Employee>> createEmployee(@Valid @RequestBody Employee employee,
                                                                    BindingResult bindingResult) {
        try {
            ResponseEntity<Messageresponse<Employee>> errorResponse = handleValidationErrors(bindingResult);
            if (errorResponse != null) return errorResponse;

            employeeService.createEmployee(employee);
            return ResponseEntity.ok(new Messageresponse<>(200, "Thêm nhân viên thành công"));
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @PutMapping("/employee/{id}")
    @Operation(summary = "Cập nhật thông tin nhân viên")
    public ResponseEntity<Messageresponse<Employee>> updateEmployee(@Valid @RequestBody Employee employee,
                                                                    BindingResult bindingResult,
                                                                    @PathVariable Integer id) {
        try {
            ResponseEntity<Messageresponse<Employee>> errorResponse = handleValidationErrors(bindingResult);
            if (errorResponse != null) return errorResponse;

            employeeService.updateEmployee(employee, id);
            return ResponseEntity.ok(new Messageresponse<>(200, "Cập nhật nhân viên thành công"));
        } catch (Exception e) {
            return handleException(e);
        }
    }
    @DeleteMapping("/employee/{id}")
    @Operation(summary = "Xóa nhân viên theo ID")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }

    @PostMapping("/employee/delete-multiple")
    @Operation(summary = "Xóa nhiều nhân viên")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteEmployees(@RequestBody List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Messageresponse<>(400, "Danh sách nhân viên cần xóa không được để trống"));
            }

            List<Integer> notFoundIds = employeeService.deleteEmployees(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(new Messageresponse<>(200, "Xóa nhân viên thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .body(new Messageresponse<>(404, "Một số nhân viên không tồn tại", notFoundIds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Messageresponse<>(500, "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    @GetMapping("/employee/export-excel")
    @Operation(summary = "Xuất danh sách nhân viên ra Excel")
    public ResponseEntity<InputStreamResource> exportEmployeesToExcel() {
        List<Employee> employees = employeeService.getAllEmployeeEntities();
        ByteArrayInputStream in = excelService.generateExcel(employees);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employees.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    private ResponseEntity<Messageresponse<Employee>> handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new Messageresponse<>(400, message));
        }
        return null;
    }

    private ResponseEntity<Messageresponse<Employee>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Messageresponse<>(409, e.getMessage()));
    }

    @PostMapping("/import")
    public String importEmployees(@RequestParam("file") MultipartFile file) {
        try {
            List<Employee> employees = parseExcel(file.getInputStream());
            // Lưu vào database
            // employeeRepository.saveAll(employees);

            return "Imported " + employees.size() + " employees!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to import: " + e.getMessage();
        }
    }

    private List<Employee> parseExcel(InputStream is) throws Exception {
        List<Employee> employees = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Bỏ qua header
            Employee emp = new Employee();
            emp.setName(row.getCell(0).getStringCellValue());
            emp.setEmail(row.getCell(1).getStringCellValue());
            emp.setPhone(row.getCell(2).getStringCellValue());
            employees.add(emp);
        }
        workbook.close();
        return employees;
    }


}
