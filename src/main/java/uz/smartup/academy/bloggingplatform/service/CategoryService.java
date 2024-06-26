package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.entity.Category;

import java.util.List;


public interface CategoryService {
    void createCategory(Category category);
    void update(Category category);
    void delete( int id);
    List<CategoryDto> getAllCategories();
    CategoryDto findCategoryById(int id);
}
