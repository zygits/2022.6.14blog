package com.zy.blog.serice;

import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.CommentParam;

public interface CommentService {
    Result findCommentById(Long id);

    Result comment(CommentParam commentParam);
}
