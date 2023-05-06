package com.zy.blog.serice;

import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {
    Result login(LoginParam loginparam);

    SysUser checkToken(String token);

    Result logOut(String token);
}
