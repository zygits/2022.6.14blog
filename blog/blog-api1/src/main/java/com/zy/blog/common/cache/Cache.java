package com.zy.blog.common.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    long expire() default 1 * 60 * 1000;

    String name() default "";

}

/**
 * 只有经常访问的才放入缓存
 * aop 定义一个切面 ，切面定义了切点和通知的关系
 *    @PostMapping("hot")
 *     @Cache(expire = 5 * 60 * 1000,name = "hot_article")
 *     public Result hotArticle(){
 *         int limit = 5;
 *         return articleService.hotArticle(limit);
 *     }
 */