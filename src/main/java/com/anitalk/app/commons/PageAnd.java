package com.anitalk.app.commons;

import lombok.Data;

@Data
public class PageAnd<T> {
    T content;
    Page page;

    @Data
    private static class Page {
        int totalPage;
    }
}
