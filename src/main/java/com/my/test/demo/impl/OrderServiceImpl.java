package com.my.test.demo.impl;

import com.my.test.demo.mongoentity.Order;
import com.my.test.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Order order) {
        mongoTemplate.save(order);
    }
}
