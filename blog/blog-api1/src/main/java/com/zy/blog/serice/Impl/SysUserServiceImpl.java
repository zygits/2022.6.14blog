package com.zy.blog.serice.Impl;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zy.blog.dao.mapper.SysUserMapper;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.ErrorCode;
import com.zy.blog.dao.vo.LoginUserVo;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.UserVo;
import com.zy.blog.serice.LoginService;
import com.zy.blog.serice.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service

public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;
    @Override
    public SysUser findUserById(Long id) {

        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser==null){
            SysUser sysUser1=new SysUser();
            sysUser.setNickname("zy");
            return  sysUser;
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SysUser::getAccount,account);
        lambdaQueryWrapper.eq(SysUser::getPassword,password);
        lambdaQueryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        lambdaQueryWrapper.last("limit 1");
        return sysUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         * 2.是否为空，解析是否成功
         * 3.redis是否存在
         * 4.如果校验失败，就返回失败
         * 5.如果校验成功，返回对应的结果
         * 6.对应结果是LoginUserVo
         */

       SysUser sysUser= loginService.checkToken(token);
       if(sysUser==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        LoginUserVo loginUserVo=new LoginUserVo();
       loginUserVo.setId(sysUser.getId().toString());
       loginUserVo.setNickname(sysUser.getNickname());
       loginUserVo.setAccount(sysUser.getAccount());
       loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getAccount,account);
        lambdaQueryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户这里 id会自动生成
        //默认生成分布式id 雪花算法
        //使用mybatisplus框架
        sysUserMapper.insert(sysUser);
    }

     @Override
    public UserVo findUserVoById(Long id) {

        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser==null){
            SysUser sysUser1=new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("zy");

        }
        UserVo userVo=new UserVo();
        userVo.setId(sysUser.getId().toString());
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        return userVo;
    }
}
