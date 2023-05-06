package com.zy.blog.controller;

import com.zy.blog.dao.vo.Result;
import com.zy.blog.serice.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private TagService tagService;
    //tags/hot
    @GetMapping("hot")
    public Result hot(){
        int limit=6;
     return   tagService.hots(limit);
    }


    @GetMapping
    public Result tags(){
    return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetails(){
        return tagService.findAllDetails();
    }


    @GetMapping("detail/{id}")
    public Result findAllDetailsById(@PathVariable("id")Long id){
        return tagService.findAllDetailsById(id);
    }
}
