package com.otabek.blog.controller;

import com.otabek.blog.service.CategoryService;
import com.otabek.blog.dto.CategoryDTO;
import java.util.List;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.update(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return categoryService.delete(id);
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDTO findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }
}
