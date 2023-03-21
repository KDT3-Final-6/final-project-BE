package com.travel.global.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageResponseDTO {
    private List<?> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int size;

    public PageResponseDTO(Page<?> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber() + 1;
        this.size = page.getSize();
    }
}
