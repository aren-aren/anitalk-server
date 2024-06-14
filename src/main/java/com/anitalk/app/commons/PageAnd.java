package com.anitalk.app.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageAnd<T> {
    List<T> content;
    PageInformation page;

    public PageAnd(Page<T> page) {
        this.content = page.getContent();
        this.page = new PageInformation(
                page.getTotalElements(),
                page.getTotalPages() - 1,
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
    }

    @Getter
    @AllArgsConstructor
    private static class PageInformation {
        Long totalElements;
        int endPage;
        int nowPage;
        int size;
        boolean first;
        boolean last;
    }
}
