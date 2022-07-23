package com.example.seppan.service;

import com.example.seppan.dao.CategoryDao;
import com.example.seppan.entity.Category;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    CategoryDao categoryDao;

    Logger logger;

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = categoryDao.findAll();
        return categories;
    }
}
