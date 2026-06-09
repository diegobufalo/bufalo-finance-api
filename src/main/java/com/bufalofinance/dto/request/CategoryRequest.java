package com.bufalofinance.dto.request;

import com.bufalofinance.entity.enums.CategoryKind;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryRequest(
        @NotBlank @Size(max = 80) String name,
        @NotNull CategoryKind kind,
        UUID parentId,
        @Size(max = 16) String color,
        @Size(max = 40) String icon
) {}
