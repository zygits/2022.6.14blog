package com.zy.blog.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zy.blog.dao.mapper")
public class MybatisPlusConfig {


      //分页插件
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
         MybatisPlusInterceptor mybatisPlusInterceptor1=new MybatisPlusInterceptor();
         mybatisPlusInterceptor1.addInnerInterceptor(new PaginationInnerInterceptor());
         return mybatisPlusInterceptor1;
    }
}
