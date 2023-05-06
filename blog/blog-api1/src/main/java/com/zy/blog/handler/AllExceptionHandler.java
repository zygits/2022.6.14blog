package com.zy.blog.handler;


import com.zy.blog.dao.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//对加了controller注解的方法进行拦截处理
@ControllerAdvice
public class AllExceptionHandler {
    //进行异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(-999,"系统异常");
        }
}
