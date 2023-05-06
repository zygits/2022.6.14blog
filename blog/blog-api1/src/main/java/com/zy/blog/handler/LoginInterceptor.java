package com.zy.blog.handler;

import com.alibaba.fastjson.JSON;
import com.zy.blog.dao.pojo.SysUser;
import com.zy.blog.dao.vo.ErrorCode;
import com.zy.blog.dao.vo.Result;
import com.zy.blog.serice.LoginService;
import com.zy.blog.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private  LoginService loginService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行方法之前进行执行   就是切片思想
        /**
         * 需要判断 请求的接口路径 是否为handlerMethod
         * 判断token是否为空
         * 不为空就进行登录验证 loginService checkToken
         * 认证成功就放行
         */

        if(!(handler instanceof HandlerMethod)){
            //handler 可能是资源handler requestResource  Springboot程序访问静态资源 默认去classpath下的static目录下去查询
            return true;
        }
        String authorization = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", authorization);
        log.info("=================request end===========================");
        if(StringUtils.isBlank(authorization)){
            Result fail = Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
            response.setContentType("application/json;charset=uft-8");
            response.getWriter().print(JSON.toJSONString(fail));
            return false;
        }
        SysUser sysUser = loginService.checkToken(authorization);
        if(sysUser==null){
            Result fail = Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
            response.setContentType("application/json;charset=uft-8");
            response.getWriter().print(JSON.toJSONString(fail));
            return false;
        }
       UserThreadLocal.put(sysUser);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /**
         * 如果不删除 ThreadLocal会有内存泄漏的风险
         */
        UserThreadLocal.remove();
    }
}
