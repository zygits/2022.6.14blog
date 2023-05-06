package com.zy.blog.serice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zy.blog.dao.mapper.CategoryMapper;
import com.zy.blog.dao.pojo.Category;
import com.zy.blog.dao.vo.CategoryVo;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.serice.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {

        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo= new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(lambdaQueryWrapper);

        return Result.success(copyList(categories));
    }

    /**
     * 查询类别细节
     * @return
     */
    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());

        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
}
