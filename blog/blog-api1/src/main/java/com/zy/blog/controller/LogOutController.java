package com.zy.blog.controller;


import com.zy.blog.dao.vo.Result;
import com.zy.blog.serice.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LogOutController {
    @Autowired
    private LoginService loginService;
    @GetMapping
    public Result logOut(@RequestHeader("Authorization") String token){
        return loginService.logOut(token);
    }
}
