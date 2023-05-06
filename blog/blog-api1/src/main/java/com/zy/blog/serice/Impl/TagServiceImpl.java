package com.zy.blog.serice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zy.blog.dao.mapper.TagMapper;
import com.zy.blog.dao.pojo.Tag;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.TagVo;
import com.zy.blog.serice.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //无法进行多表查询 自己写
         List<Tag> tags=tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    /**
     * 最热标签
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
        //标签所拥有的文章数量最多，就是最热标签
        //进行查询，需要groupby 根据tag_id进行计数 ，从大到小排列 取前limit个
        //SELECT count(*) as count,tag_id  FROM `ms_article_tag` GROUP BY tag_id ORDER BY count DESC limit 2
        //SELECT tag_id  FROM `ms_article_tag` GROUP BY tag_id ORDER BY count(*) DESC limit 2
       List<Long>  tagIds=tagMapper.findHotsTagIds(limit);
       //需求的是tagId和tagName
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.EMPTY_LIST);
        }
        //select * from tag where id in (1 2 3 4);
      List<Tag> tagList= tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(objectLambdaQueryWrapper);
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetails() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetailsById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
}
