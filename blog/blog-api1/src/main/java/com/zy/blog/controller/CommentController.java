package com.zy.blog.controller;


import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.CommentParam;
import com.zy.blog.serice.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("article/{id}")
    public Result listComment(@PathVariable Long id){
         return  commentService.findCommentById(id);
    }
    @PostMapping("create/change")
    public Result change(@RequestBody CommentParam commentParam){
      return commentService.comment(commentParam);
    }
}
