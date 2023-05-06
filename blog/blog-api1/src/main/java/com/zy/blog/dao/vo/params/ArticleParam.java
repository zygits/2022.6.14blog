package com.zy.blog.dao.vo.params;

import com.zy.blog.dao.vo.CategoryVo;
import com.zy.blog.dao.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
