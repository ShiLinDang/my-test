package com.my.test.demo.config;

import com.my.test.demo.interceptor.ApiIdempotentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//public class AddInterceptor extends WebMvcConfigurerAdapter {
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(new ApiIdempotentInterceptor())
//                .addPathPatterns("/**");
//    }
//}
