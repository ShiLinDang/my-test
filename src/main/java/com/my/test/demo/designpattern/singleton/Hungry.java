package com.my.test.demo.designpattern.singleton;

import lombok.Data;

/**
 * Description: 单例模式-饿汉模式
 *
 * @author dsl
 * @date 2019/11/1 10:46
 */
@Data
public class Hungry {

    private static Hungry hungry = new Hungry();

    /**
     * 姓名
     */
    private String name;

    /**
     * 空参构造
     */
    private Hungry (){

    }

    /**
     * 饿汉模式 直接创建对象
     * @return
     */
    public static Hungry getInstance(){
        return hungry;
    }
}
