package com.zy.blog.serice;

import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.UserVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SysUserService {
      SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
