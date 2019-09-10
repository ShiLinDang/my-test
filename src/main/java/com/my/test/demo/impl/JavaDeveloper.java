package com.my.test.demo.impl;

import com.my.test.demo.service.Developer;

/**
 * Description:动态代理测试接口实现
 *
 * @author dsl
 * @date 2019/9/10 10:30
 */
public class JavaDeveloper implements Developer {

    private String name;

    public JavaDeveloper(String name){
        this.name = name;
    }

    @Override
    public void code() {
        System.out.println(this.name + "is coding java");
    }

    @Override
    public void debug() {
        System.out.println(this.name + "is debugging java");
    }
}
