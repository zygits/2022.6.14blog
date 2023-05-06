package com.zy.blog.serice;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zy.blog.dao.mapper.ArticleMapper;
import com.zy.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class  ThreadService {


    //希望该操作在线程池中执行，不能影响主线程
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article article1=new Article();
        article1.setViewCounts(viewCounts+1);
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper=new LambdaUpdateWrapper();
        lambdaUpdateWrapper.eq(Article::getId,article.getId());
        //为了在多线程的环境下，线程安全,相当于一个自旋锁的操作
        lambdaUpdateWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(article1,lambdaUpdateWrapper);
    }
}
