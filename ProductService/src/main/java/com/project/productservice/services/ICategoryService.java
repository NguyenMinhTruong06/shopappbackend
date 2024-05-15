package com.project.productservice.services;

import com.project.productservice.dtos.CategoryDTO;
import com.project.productservice.models.Category;


import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);

    Category updateCategory(long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(long id);

    Category getCategoryById(long id);
    List<Category> getAllCategories();

}
