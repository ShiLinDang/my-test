package com.my.test.demo.runner;

import com.my.test.demo.filter.LoginFilter;
import com.my.test.demo.listener.MyHttpSessionListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:04
 */
@SpringBootApplication
@MapperScan("com.my.test.demo.dao")
@ComponentScan(basePackages = {"com.my.test.demo.*"})
public class SysUserApp {
    public static void main(String[] args) {
        SpringApplication.run(SysUserApp.class,args);
    }

    /**
     * 监听器配置
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean listenerRegist() {
        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
        srb.setListener(new MyHttpSessionListener());
        System.out.println("***************************** listener启动 ***************************");
        return srb;
    }

    /**
     * 过滤器配置
     * @return
     */
    @Bean
    public FilterRegistrationBean registerFilter() {

        FilterRegistrationBean registration = new FilterRegistrationBean();

        String errUrl = "login.html";
        registration.setFilter(loginFilter());
        // 过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        registration.addUrlPatterns("/*");
        registration.addInitParameter("ERR_URL", errUrl);
        registration.setName("WebAccessAuthorizeFilterMvc");
        System.out.println("***************************** Filter启动 ***************************");
        return registration;
    }

    @Bean
    public LoginFilter loginFilter(){
        return new LoginFilter();
    }
}
