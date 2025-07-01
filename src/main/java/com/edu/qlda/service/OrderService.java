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

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Orders> listOrders() {
        return orderRepository.findAll();
    }

    public Orders createOrder(OrderDto orderDto) {
        Orders order = new Orders();
        order.setAddress(orderDto.getAddress());
        order.setCreatedate(Date.valueOf(LocalDate.now()));
        order.setName(orderDto.getName());
        order.setUserId(orderDto.getUserId());
        order.setPhone(orderDto.getPhone());
        order.setEmail(orderDto.getEmail());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setPaymentStatus("Đang xử lý");

        orderRepository.save(order);

        List<OrderDetail> orderDetails = mapCartItemsToOrderDetails(orderDto.getCartItem(), order);
        orderDetailRepository.saveAll(orderDetails);

        return order;
    }

    private List<OrderDetail> mapCartItemsToOrderDetails(List<CartItem> cartItems, Orders order) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findByIdProduct(cartItem.getProductId());

            if (product == null) {
                // Bạn có thể ném exception hoặc xử lý sản phẩm không tồn tại tại đây
                continue;
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setNumberofproducts(cartItem.getQuantity());
            orderDetail.setPrice(product.getPrice());

            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }
}
