package com.my.test.demo.config;

import com.my.test.demo.entity.SysUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/3/2810:35
 */
@Component
public class Producer1 {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param user
     */
    public void producer1(SysUser user){
        rabbitTemplate.convertAndSend("queue1",user);
    }
}
