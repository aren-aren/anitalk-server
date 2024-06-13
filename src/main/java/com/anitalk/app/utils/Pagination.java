package com.anitalk.app.utils;

import lombok.Data;

@Data
public class Pagination {
    private Integer page;
    private Integer perPage;

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public Integer getPerPage() {
        return perPage == null ? 20 : Math.max(perPage, 100);
    }
}
