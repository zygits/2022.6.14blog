package com.zy.blog.serice.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zy.blog.dao.mapper.CommentMapper;
import com.zy.blog.dao.pojo.Comment;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.CommentVo;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.UserVo;
import com.zy.blog.dao.vo.params.CommentParam;
import com.zy.blog.serice.CommentService;
import com.zy.blog.serice.SysUserService;
import com.zy.blog.utils.UserThreadLocal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public Result findCommentById(Long id) {
        /**
         * 1.根据文章id 查询 评论列表 从comment表中查询
         * 2.根据作者id 查询作者信息
         * 判断如果level=1 去查询是否有子评论
         * 如果有根据评论id 进行查询  （parentid）
         *
         */
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId,id);
        lambdaQueryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(lambdaQueryWrapper);
        List<CommentVo> commentVos=copyList(comments);
        return Result.success(commentVos);

    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentMapper.insert(comment);
        return Result.success(null);

    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVos=new ArrayList<>();
        for (Comment c :
                comments) {
            commentVos.add(copy(c));
        }
        return commentVos;
    }

    public  CommentVo copy(Comment comment){
        CommentVo commentVo=new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //作者信息
        Long articleId = comment.getArticleId();
        UserVo userVoById = sysUserService.findUserVoById(articleId);
        commentVo.setAuthor(userVoById);
        //子评论
        Integer level = comment.getLevel();
        if(level==1){
            Long id = comment.getId();
            List<CommentVo>  commentVos=findCommentsByParentsId(id);
            commentVo.setChildrens(commentVos);
        }
        //to user
        if(level>1){
            Long toUid = comment.getToUid();
            UserVo userVoById1 = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(userVoById1);
        }

        return  commentVo;
    }

    private List<CommentVo> findCommentsByParentsId(Long id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getParentId,id);
        lambdaQueryWrapper.eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(lambdaQueryWrapper));
    }
}
