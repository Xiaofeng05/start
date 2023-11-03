package com.xiao.start.filter;

import com.alibaba.fastjson.JSON;
import com.xiao.start.common.R;
import com.xiao.start.entity.Employee;
import com.xiao.start.entity.User;
import com.xiao.start.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.xiao.start.constant.EmployeeConstant.EMPLOYEE;
import static com.xiao.start.constant.EmployeeConstant.NOT_LOGIN;
import static com.xiao.start.constant.UserConstant.USER_STATUS;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/27 1:02
 * @Description:
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        // 1.获取本次请求的URL
        String requestUrl = request.getRequestURI();
        log.info("拦截到的请求信息：{}",requestUrl);
        // 定义白名单
        String[] urls = new String []{
                // 登录页面
                // 退出页面
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs/**"
        };

        // 2. 判断本次请求是需要处理
        boolean isPass = checkUrlPass(urls, requestUrl);

        // 3. 如果不处理，直接返回呗
        if (isPass) {
            log.info("拦截到的请求信息不需要处理：{}",requestUrl);
            filterChain.doFilter(request,response);
            return;
        }


        // 4-1. 判断用户是否已经登录了，有直接放行
        if (request.getSession().getAttribute(EMPLOYEE) != null){
            long id = Thread.currentThread().getId();
            Employee employee = (Employee) request.getSession().getAttribute(EMPLOYEE);
            BaseContext.setCurrent(employee);
            filterChain.doFilter(request,response);
            return;
        }

        // 4-2. 判断用户是否已经登录了，有直接放行
        if (request.getSession().getAttribute(USER_STATUS) != null){
            long id = Thread.currentThread().getId();
            log.info("当前线程id：{}", id);
            User user = (User) request.getSession().getAttribute(USER_STATUS);
            BaseContext.setCurrent(user);
            log.info("拦截到的请求信息已经登录用户的id：{}",user.getId());
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录：{}",requestUrl);
        // 5. 如果用户未登录，直接返回数据（未登录结果），通过数据流的形式返回结果
        response.getWriter().write(JSON.toJSONString(R.error(NOT_LOGIN)));
        return;
    }


    /**
     * 路径匹配，判断是否需要处理
     * @param urls 白名单
     * @param requestUrl 请求url
     * @return  在或者不在
     */
    public boolean checkUrlPass(String[] urls, String requestUrl){
        if (StringUtils.isEmpty(requestUrl)){
            return false;
        }
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if (match){
                return true;
            }
        }
        return false;
    }


}
