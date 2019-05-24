package com.my.test.demo.runner;

import com.my.test.demo.filter.LoginFilter;
import com.my.test.demo.listener.MyHttpSessionListener;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
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

    @Bean
    public Redisson getRedisson(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + "47.98.238.150" + ":" + 6397)
                .setPassword("shi974075295")
               //设置对于master节点的连接池中连接数最大为500
               .setConnectionPoolSize(500)
               //如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒
                .setIdleConnectionTimeout(10000)
                //同任何节点建立连接时的等待超时。时间单位是毫秒。
                .setConnectTimeout(30000)
                //等待节点回复命令的时间。该时间从命令发送成功时开始计时。
               .setTimeout(3000)
               //当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
               .setReconnectionTimeout(3000)
               //在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。默认值：1500
               .setRetryInterval(3000)
               //如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。默认值：3
                .setRetryAttempts(3);
      return (Redisson) Redisson.create(config);
    }
}
