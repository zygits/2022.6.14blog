package com.zy.blog.controller;


import com.zy.blog.dao.vo.Result;
import com.zy.blog.serice.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result categories(){
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findAllDetailById(@PathVariable("id") Long id){
        return categoryService.findAllDetailById(id);
    }
}
