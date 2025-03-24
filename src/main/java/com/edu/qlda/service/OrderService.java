package com.edu.qlda.service;

import com.edu.qlda.dto.CartItem;
import com.edu.qlda.dto.OrderDto;
import com.edu.qlda.entity.OrderDetail;
import com.edu.qlda.entity.Orders;
import com.edu.qlda.entity.Product;
import com.edu.qlda.repository.OrderDetailRepository;
import com.edu.qlda.repository.OrderRepository;
import com.edu.qlda.repository.ProductRepository;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

   private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderDetailRepository orderDetailRepository;

public  OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.orderDetailRepository = orderDetailRepository;
}
    public Orders createOrder(OrderDto orderDto) {
        Orders orders = new Orders();
        orders.setAddress(orderDto.getAddress());
        orders.setName(orderDto.getName());
        orders.setUserId(orderDto.getUserId());
        orders.setPhone(orderDto.getPhone());
        orders.setEmail(orderDto.getEmail());
        orders.setTotalAmount(orderDto.getTotalAmount());
        orders.setPaymentStatus("Pending");
        orderRepository.save(orders);// hàm save vào DB có vấn đề bị nhân đôi bản ghi
        // Tạo danh sách các đối tượng OrderDetail từ CartItem
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItemdto : orderDto.getCartItem()) {
            // Tạo 1 đối tượng Orderdetai từ CartitemDto
           OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(orders);
            //Lấy thông tin sản phẩm từ cartItemDto
            Integer productId = cartItemdto.getProductId();
            Integer quantity = cartItemdto.getQuantity();
            //Tìm thông tin sản phẩm từ cơ sở dữ liệu
            Product product = productRepository.findByIdProduct(productId);
            // Kiểm tra sản phẩm có coòn trong kho không
            //...code
            // Đặt thông tin cho orderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberofproducts(quantity);
            orderDetail.setPrice(product.getPrice());
            // Thêm orderDetail vào danh sách
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return orders;
    }
}