package com.example.employeemanagement.utils;

import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.PagedResponse;
import com.example.employeemanagement.dto.response.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {

    private PaginationUtils() {
    }

    public static Pageable toPageable(PaginationRequest request) {

        return PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(
                        Sort.Direction.ASC,
                        "id"
                )
        );
    }

    public static Pageable toPageable(
            PaginationRequest request,
            Sort sort
    ) {

        return PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                sort
        );
    }

    public static <T> PagedResponse<T> toPagedResponse(Page<T> page) {

        PaginationResponse pagination =
                PaginationResponse.builder()
                        .page(page.getNumber() + 1)
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .build();

        return PagedResponse.<T>builder()
                .items(page.getContent())
                .pagination(pagination)
                .build();
    }
}