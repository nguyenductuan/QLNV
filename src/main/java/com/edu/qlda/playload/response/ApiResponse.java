package com.edu.qlda.playload.response;

import com.edu.qlda.dto.PageInfo;

public class ApiResponse <T> {
    private String message;
    private PageInfo pageInfo;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

//    public ApiResponse(String message, PageInfo pageInfo, T data) {
//        this.message = message;
//        this.pageInfo = pageInfo;
//        this.data = data;
//    }
}
