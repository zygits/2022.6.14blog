package com.zy.blog.controller;

import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.serice.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
           return sysUserService.findUserByToken(token);
    }

}
