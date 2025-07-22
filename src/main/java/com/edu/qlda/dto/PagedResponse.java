package com.edu.qlda.dto;

import java.util.List;

public class PagedResponse <T>{
    private PageInfo pageInfo;
    private List<T> data;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
