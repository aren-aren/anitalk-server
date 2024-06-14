package com.anitalk.app.utils;

import lombok.Data;

@Data
public class Pagination {
    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public Integer getSize() {
        return size == null ? 20 : Math.min(size, 100);
    }
}
