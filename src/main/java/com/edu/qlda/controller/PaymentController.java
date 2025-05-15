package com.edu.qlda.controller;

import com.edu.qlda.service.QRCodeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin("http://localhost:4200")
public class PaymentController {

    private final QRCodeService qrCodeService;

    public PaymentController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * Tạo mã QR thanh toán từ số tài khoản và số tiền.
     * @param amount  Số tiền cần thanh toán
     * @param account Số tài khoản nhận tiền
     * @return Chuỗi base64 ảnh QR code
     */
    @GetMapping("/qr-code")
    public String generateQRCode(@RequestParam String amount, @RequestParam String account) {
        String paymentInfo = String.format("STK:%s; AMT:%s", account, amount);
        String base64Image = qrCodeService.generateQRCode(paymentInfo, 250, 250);
        return "data:image/png;base64," + base64Image;
    }
}
