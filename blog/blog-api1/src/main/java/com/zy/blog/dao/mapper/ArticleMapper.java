package com.zy.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.blog.dao.dos.Archievs;
import com.zy.blog.dao.pojo.Article;
import com.zy.blog.dao.vo.Result;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {
    List<Archievs> listArchives();

    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month);
}
