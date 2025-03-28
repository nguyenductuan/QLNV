package com.edu.qlda.service;

import com.edu.qlda.entity.Employee;
import com.edu.qlda.exception.ValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public ByteArrayInputStream generateExcel(List<Employee> employees) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Employees");
            int rowIdx = 0;
            // Tạo tiêu đề lớn
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Danh sách nhân viên");

            // Style cho tiêu đề lớn
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            //Bổ sung căn giữa text tiêu đề
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            titleCell.setCellStyle(titleStyle);

            // Merge ô tiêu đề (từ cột 0 đến 8)
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Dòng tiêu đề cột
            Row headerRow = sheet.createRow(rowIdx++);
            String[] columns = {"ID", "Name", "Email", "Chức vụ", "Giới tính", "Ngày sinh", "Số điện thoại", "Quyền", "Địa chỉ"};

            // Style cho tiêu đề cột
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Thêm tiêu đề cột
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Style cho dữ liệu (xuống dòng)
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);

            // Ghi dữ liệu nhân viên
            for (Employee employee : employees) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(employee.getEmployeeId());
                row.createCell(1).setCellValue(employee.getName());
                row.createCell(2).setCellValue(employee.getEmail());
                row.createCell(3).setCellValue(employee.getPosition().getName());
                row.createCell(4).setCellValue(employee.getGender() == 1 ? "Nam" : "Nữ");
                row.createCell(5).setCellValue(employee.getBirthday().toString());
                row.createCell(6).setCellValue(employee.getPhone());
                row.createCell(7).setCellValue(employee.getRole().getName());
                row.createCell(8).setCellValue(employee.getAddress());

                for (int i = 0; i < columns.length; i++) {
                    row.getCell(i).setCellStyle(wrapStyle);
                }
            }
            // Tự động điều chỉnh độ rộng cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}