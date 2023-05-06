package com.zy.blog.controller;


import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.LoginParam;
import com.zy.blog.serice.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result login(@RequestBody LoginParam loginparam){
        //登录  验证用户 访问用户表

        return loginService.login(loginparam);
    }
}
/**
 * 用户登录流程梳理
 * 但是这个项目做的时候是先实现登录功能，导致在和数据库比对password的时候，对password进行了加密，所以注册时候也要对password进行同样的加密，所以我觉得应该先实现注册功能。
 * 1.使用loginController来进行映射，前端传入account和password，后端使用loginParam来接收前端的RequestBody
 * 2.使用loginService对前端传入的account和password进行操作
 * 3.在loginService中使用SysUserService进行操作
 * 4.获取account和password，其中password要使用盐进行加密，如果登录成功，然后使用jwt令牌进行加密，放入redis中，设置一天的过期时间
 * 5.在登入时候使用SysUserService中和数据库进行交互，查找数据库中的信息进行比对。
 */