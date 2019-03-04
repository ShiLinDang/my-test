package com.my.test.demo.runner;

import com.my.test.demo.filter.LoginFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:04
 */
@SpringBootApplication
@MapperScan("com.my.test.demo.dao")// 扫描mapper映射实体
@ComponentScan(basePackages = {"com.my.test.demo.*"})
public class SysUserApp {
    public static void main(String[] args) {
        SpringApplication.run(SysUserApp.class,args);
    }

    @Bean
    public FilterRegistrationBean registerFilter() {

        FilterRegistrationBean registration = new FilterRegistrationBean();

        String errUrl = "login.html";
        registration.setFilter(loginFilter());
        // 过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        registration.addUrlPatterns("/*");
        registration.addInitParameter("ERR_URL", errUrl);
        registration.setName("WebAccessAuthorizeFilterMvc");

        return registration;
    }

    @Bean
    public LoginFilter loginFilter(){
        return new LoginFilter();
    }
}
