package com.edu.qlda.dto;



public class DiscountDto {
    private Integer totalAmount;
    private Integer selectedDiscount;

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
}
