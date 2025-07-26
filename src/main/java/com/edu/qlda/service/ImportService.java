package com.edu.qlda.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
@Service
public class ImportService {
    public void processFile(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(1);
            for (Row row : sheet) {
                // Bỏ qua dòng tiêu đề
                if (row.getRowNum() == 1) continue;

                String name = row.getCell(1).getStringCellValue();
               // int age = (int) row.getCell(1).getNumericCellValue();

                System.out.println("Tên: " + name);

                // Có thể lưu vào database tại đây
            }
        }
    }
}
