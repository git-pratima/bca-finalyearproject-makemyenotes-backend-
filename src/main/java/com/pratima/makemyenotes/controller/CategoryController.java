package com.pratima.makemyenotes.controller;
import com.pratima.makemyenotes.constants.AppConstants;
import com.pratima.makemyenotes.dto.CategoryDTO;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping("/add")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @PostMapping("/update")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllCategories(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE) int page,
                                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String orderBy,
                                                     @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_ORDER) String order,
                                                     @RequestParam(value ="category", required = false, defaultValue = "") String category,
                                                     @RequestParam(value ="subCategory", required = false, defaultValue = "") String subCategory,
                                                     @RequestParam(value ="topic", required = false, defaultValue = "") String topic,
                                                     @RequestParam(value ="active", required = false, defaultValue = "") String active) {
        return ResponseEntity.ok(categoryService.getAllCategories(page,size,orderBy,order,category,subCategory,topic,active));
    }

    @GetMapping("/get-category-list")
    public ResponseEntity<Response> getCategoryList(){

        List<String> categoryList =  categoryService.getAllCategoryList();
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .categoryList(categoryList)
                .build());

    }

    @GetMapping("/get-subcategory-list/{cat}")
    public ResponseEntity<Response> getSubCategoryList(@PathVariable String cat) {

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .subCategoryList(categoryService.getSubCategoryByCategory(cat))
                .build());
    }

    @GetMapping("/get-topic-list/{cat}")
    public ResponseEntity<Response> getTopicList(@PathVariable String cat) {

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .topicList(categoryService.findTopicBySubCategory(cat))
                .build());
    }

    @GetMapping("/user-category-list")
    public ResponseEntity<Response> getUserCategoryList(){

        List<String> categoryList =  categoryService.getUserCategoryList();
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .categoryList(categoryList)
                .build());

    }

    @GetMapping("/get-category/{id}")
    public ResponseEntity<Response> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @DeleteMapping("/delete-category/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @GetMapping("/get-user-subcategory-list/{cat}")
    public ResponseEntity<Response> getUserSubCategoryList(@PathVariable String cat) {

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .subCategoryList(categoryService.getUserSubCategoryByCategory(cat))
                .build());
    }

    @GetMapping("/get-user-topic-list/{cat}")
    public ResponseEntity<Response> getTopicBySubCategoryAndUserid(@PathVariable String cat) {

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("success")
                .topicList(categoryService.findTopicBySubCategoryAndUserid(cat))
                .build());
    }
}
