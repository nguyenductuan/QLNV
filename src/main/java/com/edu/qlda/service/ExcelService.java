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

    private static final String[] COLUMNS = {
            "ID", "Name", "Email", "Chức vụ", "Giới tính", "Ngày sinh", "Số điện thoại", "Quyền", "Địa chỉ"
    };

    public ByteArrayInputStream generateExcel(List<Employee> employees) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Employees");
            int rowIdx = 0;

            // Title row
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Danh sách nhân viên");
            titleCell.setCellStyle(createTitleStyle(workbook));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, COLUMNS.length - 1));

            // Header row
            Row headerRow = sheet.createRow(rowIdx++);
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < COLUMNS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNS[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            CellStyle wrapStyle = createWrapStyle(workbook);
            for (Employee emp : employees) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(emp.getEmployeeId());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getEmail());
                row.createCell(3).setCellValue(emp.getPosition().getName());
                row.createCell(4).setCellValue(emp.getGender() == 1 ? "Nam" : "Nữ");
                row.createCell(5).setCellValue(emp.getBirthday().toString());
                row.createCell(6).setCellValue(emp.getPhone());
                row.createCell(7).setCellValue(emp.getRole().getName());
                row.createCell(8).setCellValue(emp.getAddress());

                for (int i = 0; i < COLUMNS.length; i++) {
                    row.getCell(i).setCellStyle(wrapStyle);
                }
            }

            // Auto resize columns
            for (int i = 0; i < COLUMNS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new ValidationException("Lỗi khi xuất file Excel: " + e.getMessage());
        }
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createWrapStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }
}
