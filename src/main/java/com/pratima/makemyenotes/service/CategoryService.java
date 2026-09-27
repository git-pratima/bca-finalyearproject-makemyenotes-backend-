package com.pratima.makemyenotes.service;

import com.pratima.makemyenotes.dto.CategoryDTO;
import com.pratima.makemyenotes.dto.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CategoryService {
    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories(int page, int size, String sortBy, String direction, String category, String subcategory,String topic,String active);

    Response getCategoryById(Long id);

    Response updateCategory(CategoryDTO categoryDTO);

    Response deleteCategory(Long id);

    List<String> getAllCategoryList();

    Map<String, String> getCategoryByCategory(String category);

    Map<String, Long> getCategoryCount();

    List<String> getSubCategoryByCategory(String cat);

    List<String> findTopicBySubCategory(String subCat);

    List<String> getUserCategoryList();

    List<String> getUserSubCategoryByCategory(String category);

    List<String> findTopicBySubCategoryAndUserid(String cat);
}
