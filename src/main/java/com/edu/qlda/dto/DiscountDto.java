package com.edu.qlda.dto;

/**
 * DTO class for discount details, including total amount and selected discount.
 */
public class DiscountDto {

    private Integer totalAmount;
    private Integer selectedDiscount;

    // Constructors
    public DiscountDto() {
    }

    public DiscountDto(Integer totalAmount, Integer selectedDiscount) {
        this.totalAmount = totalAmount;
        this.selectedDiscount = selectedDiscount;
    }

    // Getters and Setters
    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getSelectedDiscount() {
        return selectedDiscount;
    }

    public void setSelectedDiscount(Integer selectedDiscount) {
        this.selectedDiscount = selectedDiscount;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "DiscountDto{" +
                "totalAmount=" + totalAmount +
                ", selectedDiscount=" + selectedDiscount +
                '}';
    }
}
