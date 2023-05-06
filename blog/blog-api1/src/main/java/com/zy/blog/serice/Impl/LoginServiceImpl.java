package com.zy.blog.serice.Impl;


import com.alibaba.fastjson.JSON;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.ErrorCode;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.LoginParam;
import com.zy.blog.serice.LoginService;
import com.zy.blog.serice.SysUserService;
import com.zy.blog.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    private static final String salt="zy";
    @Override
    public Result login(LoginParam loginparam) {
        //检查参数是否合法
        //根据用户名和密码去user表中查询是否存在
        //如果存在使用jwt生成token返回前端
        //token放入redis
        String account = loginparam.getAccount();
        String password = loginparam.getPassword();
        //检查参数是否合法
        if(account==null||password==null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password= DigestUtils.md5Hex(password+salt);
        SysUser sysUser=sysUserService.findUser(account,password);

        if(sysUser==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if(StringUtils.isBlank(token)){
           return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null){
            return null;
        }
        String s = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(s)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(s, SysUser.class);
        return  sysUser;
    }

    @Override
    public Result logOut(String token) {
       redisTemplate.delete("TOKEN_"+token);
       return Result.success(null);
    }
}
