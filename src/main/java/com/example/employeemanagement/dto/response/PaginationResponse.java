package com.example.employeemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationResponse {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}