package com.otabek.blog.service;

import org.springframework.stereotype.Service;

import com.otabek.blog.dto.CategoryDTO;
import com.otabek.blog.repository.CategoryRepository;
import com.otabek.blog.entity.Category;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    public CategoryDTO create(CategoryDTO categoryDTO){
        categoryRepository.findByName(categoryDTO.getName()).orElseThrow(() -> new RuntimeException("Category already exists"));
        Category category = mapToEntity(categoryDTO);
        categoryRepository.save(category);
        return mapToDTO(category);
    }
    
    public CategoryDTO update(Long id, CategoryDTO categoryDTO){
        categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        Category category = mapToEntity(categoryDTO);
        categoryRepository.save(category);
        return mapToDTO(category);
    }
    
    public boolean delete(Long id){
        categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.deleteById(id);
        return true;
    }
    
    public CategoryDTO findById(Long id){
        return categoryRepository.findById(id).map(this::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    
    public List<CategoryDTO> getAll(){
        return categoryRepository.findAll().stream()
            .map(this::mapToDTO)
            .toList();
    }
    
    private Category mapToEntity(CategoryDTO categoryDTO){
        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();
    }
    
    private CategoryDTO mapToDTO(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}