package com.zy.blog.serice.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zy.blog.dao.dos.Archievs;
import com.zy.blog.dao.mapper.ArticleBodyMapper;
import com.zy.blog.dao.mapper.ArticleMapper;
import com.zy.blog.dao.mapper.ArticleTagMapper;
import com.zy.blog.dao.pojo.Article;
import com.zy.blog.dao.pojo.ArticleBody;
import com.zy.blog.dao.pojo.ArticleTag;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.ArticleBodyVo;
import com.zy.blog.dao.vo.ArticleVo;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.TagVo;
import com.zy.blog.dao.vo.params.ArticleParam;
import com.zy.blog.dao.vo.params.PageParams;
import com.zy.blog.serice.*;
import com.zy.blog.utils.UserThreadLocal;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page=new Page<>(pageParams.getPage(),pageParams.getPageSize());
      IPage<Article>articleIPage= articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }
    //    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1.分页查询   article数据库表
//         */
//        Page<Article> page=new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        if(pageParams.getCategoryId()!=null){
//            lambdaQueryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList=new ArrayList<>();
//        if(pageParams.getTagId()!=null){
//            //article表中并没有tag字段，因为一篇文章有多个标签
//            //在article——tag表中，有一对多的关系
//            LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
//            lambdaQueryWrapper1.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(lambdaQueryWrapper1);
//            for (ArticleTag a :
//                    articleTags) {
//                articleIdList.add(a.getArticleId());
//            }
//            if(articleIdList.size()>0){
//                //and in (1,2,3)
//             lambdaQueryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        //是否置顶排序
//        lambdaQueryWrapper.orderByDesc(Article::getWeight);
//        // order by create_data desc;
//        lambdaQueryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, lambdaQueryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList=copyList(records,true,true);
//        return Result.success(records);
//    }

    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
          lambdaQueryWrapper.select(Article::getId,Article::getTitle);
          lambdaQueryWrapper.last("limit"+limit);
        List<Article> articles = articleMapper.selectList(lambdaQueryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Article::getCreateDate);
        lambdaQueryWrapper.select(Article::getId,Article::getTitle);
        lambdaQueryWrapper.last("limit"+limit);
        List<Article> articles = articleMapper.selectList(lambdaQueryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    /**
     * 文章归档
     * @return
     */
    @Override
    public Result listArchieves() {
        List<Archievs> archievs= articleMapper.listArchives();
         return Result.success(archievs);
    }
    @Autowired
    private ThreadService threadService;
    /**
     * 展示文章
     * @param id
     * @return
     */
    @Override
    public Result findArticleById(Long id) {
        /**
         * 根据id查询文章信息
         */

        Article article=articleMapper.selectById(id);
        ArticleVo articleVo=copy(article,true,true,true,true);
        /**
         * 查看完文章新增阅读数
         */
        //更新的话 就是增加此次接口的耗时
        //使用线程池，把更新操作放入线程池和主线程不相干了
          threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser= UserThreadLocal.get();
        /**
         * 发布文章
         * 构建article 从前端传过来的数据进行处理，赋值，放入数据库，同时响应前端
         * 作者id 即当前登录用户 使用userThreadLocal拿 但是如果需要使用该UserThreadLocal就需要将这个接口加入到登录拦截器中
         * 标签 要将标签加入到 关联列表
         * 内容存储
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        //插入之后生成文章id
        articleMapper.insert(article);


        //tag
        List<TagVo> tags =  articleParam.getTags();
        if(tags!=null){
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId().toString());
        return Result.success(articleVo);
    }


    private  List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor){
           List<ArticleVo> articleVoList=new ArrayList<>();
           for(Article record:records){
               articleVoList.add(copy(record,true,true,false,false));
           }
           return articleVoList;
    }

    private  List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        List<ArticleVo> articleVoList=new ArrayList<>();
        for(Article record:records){
            articleVoList.add(copy(record,true,true,isBody,isCategory));
        }
        return articleVoList;
    }
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo=new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口，都需要标签
        if(isTag){
            Long id = article.getId();
            List<TagVo> tagsByArticleId = tagService.findTagsByArticleId(id);
            articleVo.setTags(tagsByArticleId);
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            SysUser userById = sysUserService.findUserById(authorId);
            String nickname = userById.getNickname();
            articleVo.setAuthor(nickname);
        }
        if(isBody){
            Long bodyId=article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
             Long categoryId=article.getCategoryId();
             articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }
       @Autowired
       private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo=new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
