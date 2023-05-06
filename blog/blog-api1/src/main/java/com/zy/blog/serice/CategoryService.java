package com.zy.blog.serice;

import com.zy.blog.dao.vo.CategoryVo;
import com.zy.blog.dao.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result findAllDetailById(Long id);
}
