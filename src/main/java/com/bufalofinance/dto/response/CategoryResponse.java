package com.bufalofinance.dto.response;

import com.bufalofinance.entity.Category;
import com.bufalofinance.entity.enums.CategoryKind;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        CategoryKind kind,
        UUID parentId,
        String color,
        String icon
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(
                c.getId(), c.getName(), c.getKind(),
                c.getParentId(), c.getColor(), c.getIcon()
        );
    }
}
