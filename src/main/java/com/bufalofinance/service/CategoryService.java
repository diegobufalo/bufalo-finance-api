package com.bufalofinance.service;

import com.bufalofinance.dto.request.CategoryRequest;
import com.bufalofinance.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryResponse> findAll(UUID userId);

    CategoryResponse findById(UUID id, UUID userId);

    CategoryResponse create(CategoryRequest request, UUID userId);

    CategoryResponse update(UUID id, CategoryRequest request, UUID userId);

    void delete(UUID id, UUID userId);
}
