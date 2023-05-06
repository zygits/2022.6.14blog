package com.zy.blog.serice;

import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetails();

    Result findAllDetailsById(Long id);
}
