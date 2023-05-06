package com.zy.blog.serice;

import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.ArticleParam;
import com.zy.blog.dao.vo.params.PageParams;

public interface ArticleService {
    Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticle(int limit);

    Result listArchieves();

    Result findArticleById(Long id);

    Result publish(ArticleParam articleParam);
}
