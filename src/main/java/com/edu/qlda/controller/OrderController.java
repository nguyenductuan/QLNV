package com.edu.qlda.controller;

import com.edu.qlda.dto.OrderDto;
import com.edu.qlda.dto.PageInfo;
import com.edu.qlda.entity.Orders;
import com.edu.qlda.entity.Product;
import com.edu.qlda.playload.response.ApiResponse;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
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
//    @GetMapping
//    @Operation(summary = "Lấy danh sách đơn hàng")
//    public ResponseEntity<Messageresponse<List<Orders>>> getAllOrders() {
//        List<Orders> orders = orderService.listOrder();
//        Messageresponse<List<Orders>> response  = new Messageresponse<>(200, "Lấy danh sách đơn hàng thành công", orders);
//        return ResponseEntity.ok(response);
//
//    }

    @GetMapping ()
    public ResponseEntity<ApiResponse<List<Orders>>> getAllOrders(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam (defaultValue = "1")   Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Orders> page = orderService.listOrder(pageable);
        PageInfo pageInfo =  new PageInfo();

        pageInfo.setPageNo( page.getNumber() + 1 );
        pageInfo.setPageSize(page.getSize());
        pageInfo.setTotalCount(page.getTotalElements());
        pageInfo.setTotalPage(page.getTotalPages());

        ApiResponse<List<Orders>> response = new ApiResponse<>();
        response.setMessage("Successfully!");
        response.setPageInfo(pageInfo);
        response.setData(page.getContent());

        return ResponseEntity.ok(response);
    }
}
