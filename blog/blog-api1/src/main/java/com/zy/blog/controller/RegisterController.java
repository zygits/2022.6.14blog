package com.zy.blog.controller;


import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.LoginParam;
import com.zy.blog.serice.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping
    public Result register(@RequestBody LoginParam loginParam){
         return registerService.register(loginParam);
    }
}
