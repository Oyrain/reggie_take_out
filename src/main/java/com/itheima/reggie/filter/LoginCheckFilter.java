package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到的请求：{}",requestURI);

        //定义不需要处理的uri
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs",
                "/order/**"
        };

        //2. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(urls, requestURI);

        //3.如果不需要，则直接放行
        if(check){
            log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4.判断登录状态，如果已登录，则直接放行
        Object employee = request.getSession().getAttribute("employee");
        if(employee != null){
            log.info("用户已登录,用户id为:{}",employee);

            Long empid = (Long)employee;
            BaseContext.setCurrentId(empid);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");

        //5.如果未登录, 则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查此次请求是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


}
