package com.edu.qlda.service;

import com.edu.qlda.exception.ValidationException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodeService {

    public String generateQRCode(String content, int width, int height) {
        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            }

        } catch (WriterException | IOException e) {
            throw new ValidationException("Lỗi khi tạo mã QR: " + e.getMessage());
        }
    }
}
