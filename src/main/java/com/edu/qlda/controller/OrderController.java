package com.edu.qlda.controller;
import com.edu.qlda.dto.OrderDto;
import com.edu.qlda.entity.Orders;
import com.edu.qlda.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class OrderController {

    private final  OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("addorder")
    public Orders createOrder(@RequestBody OrderDto orderDto) {
     return orderService.createOrder(orderDto);
    }
    @GetMapping("listOrder")
    public List<Orders> getAllOrders() {
        return orderService.listOrder();
    }
}
