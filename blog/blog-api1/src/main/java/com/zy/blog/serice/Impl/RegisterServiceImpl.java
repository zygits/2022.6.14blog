package com.zy.blog.serice.Impl;

import com.alibaba.fastjson.JSON;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.ErrorCode;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.LoginParam;
import com.zy.blog.serice.RegisterService;
import com.zy.blog.serice.SysUserService;
import com.zy.blog.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RedisTemplate redisTemplate;
    private static  final String slat="zy";
    @Autowired
    private SysUserService sysUserService;
    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 参数是否合法
         * 账户是否存在  存在就账户已注册
         * 如果账户不存在，就注册用户
         * 生成token传入redis返回
         * 注意加上事务
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //判断是否存在
        SysUser user = sysUserService.findUserByAccount(account);
        if(user!=null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
       SysUser sysUser=new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);
        //token
        String token = JWTUtils.createToken(sysUser.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
