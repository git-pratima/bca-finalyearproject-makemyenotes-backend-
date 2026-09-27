package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByCategoryContainingAndSubCategoryContainingAndTopicContainingAndActiveContainingAndUserId(
            String category,
            String subcategory,
            String topic,
            String active,
            Long userId,
            Pageable pageable
    );

    @Query("SELECT DISTINCT c.category FROM Category c")
    List<String> findAllCategoryNames();

    @Query("SELECT DISTINCT c.category FROM Category c where c.user.id=:userId")
    List<String> findUserCategoryList(@Param("userId")  Long userId);

    @Query("SELECT DISTINCT c.subCategory FROM Category c WHERE c.category = :category")
    List<String> findSubCategoryByCategory(@Param("category") String category);

    @Query("SELECT DISTINCT c.subCategory FROM Category c WHERE c.category = :category AND c.user.id=:userId")
    List<String> findSubCategoryByCategoryAndUserid(@Param("category") String category, @Param("userId") Long userId);

    @Query("SELECT DISTINCT c.topic FROM Category c WHERE c.subCategory = :subCategory")
    List<String> findTopicBySubCategory(@Param("subCategory") String subCategory);

    @Query("SELECT DISTINCT c.topic FROM Category c WHERE c.subCategory = :subCategory AND c.user.id=:userId")
    List<String> findTopicBySubCategoryAndUserid( @Param("subCategory") String subCategory, @Param("userId")Long userId);

    Optional<Category> findByCategory(String category);

    Category findByCategoryAndSubCategoryAndTopic(String category, String subCategory, String topic);

    Category findByCategoryAndSubCategoryAndTopicAndUserId(String category, String subCategory, String topic, Long userId);

}
