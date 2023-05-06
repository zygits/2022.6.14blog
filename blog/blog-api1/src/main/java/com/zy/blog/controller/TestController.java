package com.zy.blog.controller;


import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.utils.UserThreadLocal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @PostMapping
    public Result Test(){
        SysUser sysUser= UserThreadLocal.get();
        return Result.success(sysUser);
    }
}
