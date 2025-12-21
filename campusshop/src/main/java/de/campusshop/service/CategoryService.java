package de.campusshop.service;

import de.campusshop.dto.CategoryDto;
import de.campusshop.model.Category;
import de.campusshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryService:
 * - kapselt Datenzugriff
 * - macht Mapping Entity -> DTO
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private CategoryDto toDto(Category c) {
        CategoryDto dto = new CategoryDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setSlug(c.getSlug());

        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        dto.setVersion(c.getVersion());

        return dto;
    }
}
