package com.zy.blog.serice;

import com.zy.blog.dao.vo.Result;
import com.zy.blog.dao.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RegisterService {
    Result register(LoginParam loginParam);
}
