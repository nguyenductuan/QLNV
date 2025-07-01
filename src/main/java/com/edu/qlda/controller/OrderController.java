package com.edu.qlda.controller;

import com.edu.qlda.dto.OrderDto;
import com.edu.qlda.entity.Orders;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("http://localhost:4200")
@Tag(name = "Order Controller", description = "API quản lý đơn hàng")

public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Thêm mới đơn hàng")
    public ResponseEntity<Messageresponse<Orders>> createOrder(@RequestBody OrderDto orderDto) {
        Orders order = orderService.createOrder(orderDto);
        Messageresponse<Orders> response = new Messageresponse<>(200, "Tạo đơn hàng thành công", order);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    @Operation(summary = "Lấy danh sách đơn hàng")
    public ResponseEntity<Messageresponse<List<Orders>>> getAllOrders() {
        List<Orders> orders = orderService.listOrder();
        Messageresponse<List<Orders>> response  = new Messageresponse<>(200, "Lấy danh sách đơn hàng thành công", orders);
        return ResponseEntity.ok(response);

    }
}
