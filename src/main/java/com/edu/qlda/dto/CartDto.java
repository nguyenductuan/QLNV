package com.edu.qlda.dto;

/**
 * Data Transfer Object (DTO) for Cart.
 * Represents an item in the shopping cart.
 */
public class CartDto {

    private String productId;
    private Integer cartId;
    private Integer quantity;
    private Integer employeeId;

    // Constructors
    public CartDto() {
    }

    public CartDto(String productId, Integer cartId, Integer quantity, Integer employeeId) {
        this.productId = productId;
        this.cartId = cartId;
        this.quantity = quantity;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "CartDto{" +
                "productId='" + productId + '\'' +
                ", cartId=" + cartId +
                ", quantity=" + quantity +
                ", employeeId=" + employeeId +
                '}';
    }
}
