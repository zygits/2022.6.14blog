package com.zy.blog.controller;

import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.ArticleParam;
import com.zy.blog.dao.vo.params.PageParams;
import com.zy.blog.serice.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){
         return articleService.listArticle(pageParams);
    }


    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("hot")
    public Result hotArticle(){
        int limit=5;
            return articleService.hotArticle(limit);
    }

    @PostMapping("new")
    public Result newArticle(){
        int limit=5;
        return articleService.newArticle(limit);
    }

    @PostMapping("listArchives")
    public Result listArchieves(){
        return articleService.listArchieves();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
         return articleService.findArticleById(id);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
