package com.bufalofinance.controller;

import com.bufalofinance.dto.request.CategoryRequest;
import com.bufalofinance.dto.response.CategoryResponse;
import com.bufalofinance.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> list(Authentication auth) {
        return categoryService.findAll(userId(auth));
    }

    @GetMapping("/{id}")
    public CategoryResponse get(@PathVariable UUID id, Authentication auth) {
        return categoryService.findById(id, userId(auth));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(request, userId(auth)));
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request,
            Authentication auth) {
        return categoryService.update(id, request, userId(auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication auth) {
        categoryService.delete(id, userId(auth));
        return ResponseEntity.noContent().build();
    }

    private UUID userId(Authentication auth) {
        return (UUID) auth.getPrincipal();
    }
}
