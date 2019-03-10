package com.my.test.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Interceptor 在AOP（Aspect-Oriented Programming）中用于在某个方法或字段被访问之前，
 * 进行拦截然后在之前或之后加入某些操作。
 * 比如日志，安全等。一般拦截器方法都是通过动态代理的方式实现。
 * 可以通过它来进行权限验证，或者判断用户是否登陆
 */
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("********************************** preHandle被调用 **************************");
        Map map =(Map)httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        System.out.println(map.get("realName"));
        if(map.get("realName").equals("李白")) {
            return true;    //如果false，停止流程，api被拦截
        }else {
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.write("please login again!");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("********************************** postHandle被调用 **************************");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("********************************** afterCompletion被调用 **************************");
    }
}
