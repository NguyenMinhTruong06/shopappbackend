package com.project.productservice.controller;


import com.project.productservice.dtos.CategoryDTO;
import com.project.productservice.models.Category;
import com.project.productservice.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")

@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping("/post")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);

        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert Category successfully"+categoryDTO);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable long id,@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("update thàng công");
    }
    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Xoá thàng công id "+id);
    }
}
