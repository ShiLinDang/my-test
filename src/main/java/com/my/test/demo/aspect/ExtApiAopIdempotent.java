package com.my.test.demo.aspect;

import com.my.test.demo.annotation.ApiIdempotent;
import com.my.test.demo.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class ExtApiAopIdempotent {

    @Autowired
    private JedisUtil jedisUtil;

    private String TOKEN_NAME = "token";

    @Pointcut("execution(* com.my.test.demo.controller..*.*(..))")
    public void pointCut(){}

    // 前置通知转发Token参数  进行拦截的逻辑
    @Before("pointCut()")
    public void before(JoinPoint point) {
        System.out.println(("==========================================>处理token切入点前执行"));
        //获取并判断方法上是否有注解
        MethodSignature signature = (MethodSignature) point.getSignature();//统一的返回值
        ApiIdempotent apiIdempotent = signature.getMethod().getDeclaredAnnotation(ApiIdempotent.class);//参数是注解的那个
        if (apiIdempotent != null) { //如果有注解的情况
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            // 验证token
            String token = request.getHeader(TOKEN_NAME);
            if (StringUtils.isBlank(token)) {// header中不存在token
                token = request.getParameter(TOKEN_NAME);
                if (StringUtils.isBlank(token)) {// parameter中也不存在token
                    throw new RuntimeException("token错误");
                }
            }

            Long del = jedisUtil.del(token);
            if (del <= 0) {
                throw new RuntimeException("token错误");
            }
        }
    }

    /**
     * 在切入点之后执行
     * @param response
     */
    @AfterReturning(returning = "response",pointcut = "pointCut()")
    public void doAfterReturn(Object response){
        //记录返回值
        System.out.println(("==========================================>处理token切入点后执行"));
    }
}
