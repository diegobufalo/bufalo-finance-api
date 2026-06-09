package com.bufalofinance.service.impl;

import com.bufalofinance.dto.request.CategoryRequest;
import com.bufalofinance.dto.response.CategoryResponse;
import com.bufalofinance.entity.Category;
import com.bufalofinance.repository.CategoryRepository;
import com.bufalofinance.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> findAll(UUID userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public CategoryResponse findById(UUID id, UUID userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .map(CategoryResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest req, UUID userId) {
        validateParent(req.parentId(), userId);
        Category category = new Category();
        applyRequest(category, req);
        category.setUserId(userId);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest req, UUID userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        validateParent(req.parentId(), userId);
        applyRequest(category, req);
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        categoryRepository.delete(category);
    }

    private void applyRequest(Category category, CategoryRequest req) {
        category.setName(req.name());
        category.setKind(req.kind());
        category.setParentId(req.parentId());
        category.setColor(req.color());
        category.setIcon(req.icon());
    }

    private void validateParent(UUID parentId, UUID userId) {
        if (parentId != null && categoryRepository.findByIdAndUserId(parentId, userId).isEmpty()) {
            throw new IllegalArgumentException("Categoria pai não encontrada");
        }
    }
}
