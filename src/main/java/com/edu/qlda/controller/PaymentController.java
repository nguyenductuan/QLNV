package com.edu.qlda.controller;

import com.edu.qlda.service.QRCodeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
public class PaymentController {
    private final QRCodeService qrCodeService;

    public PaymentController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/genQR")
    public String getPaymentQRCode(@RequestParam String amount, @RequestParam String account) {
        String paymentInfo = "STK:" + account + "; AMT:" + amount;
        return "data:image/png;base64," + qrCodeService.generateQRCode(paymentInfo, 250, 250);
    }
}
