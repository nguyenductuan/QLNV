package com.edu.qlda.controller;
import com.edu.qlda.dto.OrderDto;
import com.edu.qlda.entity.Orders;
import com.edu.qlda.service.OrderService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
