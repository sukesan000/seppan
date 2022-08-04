package com.example.seppan.dao;

import com.example.seppan.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    @Query(value = "select name from categories where category_id = :cId", nativeQuery = true)
    String findNameById(int cId);
}
