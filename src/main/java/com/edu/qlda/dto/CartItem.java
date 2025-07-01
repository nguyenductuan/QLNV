package com.edu.qlda.dto;

/**
 * Represents an item in the shopping cart.
 */
public class CartItem {

    private Integer productId;
    private Integer quantity;

    // Constructors
    public CartItem() {
    }

    public CartItem(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
