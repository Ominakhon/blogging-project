package uz.smartup.academy.bloggingplatform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.CategoryDao;
import uz.smartup.academy.bloggingplatform.dto.CategoryDto;
import uz.smartup.academy.bloggingplatform.dto.CategoryDtoUtil;
import uz.smartup.academy.bloggingplatform.entity.Category;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDtoUtil categoryDtoUtil;
    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDtoUtil categoryDtoUtil, CategoryDao categoryDao) {
        this.categoryDtoUtil = categoryDtoUtil;
        this.categoryDao = categoryDao;
    }

    @Transactional
    @Override
    public void createCategory(CategoryDto categoryDto) {
        Category category=categoryDtoUtil.toEntity(categoryDto);
        categoryDao.save(category);

    }

    @Transactional
    @Override
    public void update(CategoryDto categoryDto) {
        Category category=categoryDtoUtil.toEntity(categoryDto);
        categoryDao.update(category);
    }

    @Transactional
    @Override
    public void delete(int id) {
        categoryDao.delete(categoryDao.findCategoryById(id));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryDtoUtil.toDTOs(categoryDao.getAllCategories());
    }

    @Override
    public CategoryDto findCategoryById(int id) {
        return categoryDtoUtil.toDto(categoryDao.findCategoryById(id));
    }
}
