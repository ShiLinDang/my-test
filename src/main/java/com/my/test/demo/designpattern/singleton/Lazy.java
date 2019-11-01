package com.my.test.demo.designpattern.singleton;

import lombok.Data;

/**
 * Description:单例模式-懒汉模式
 *
 * @author dsl
 * @date 2019/11/1 10:49
 */
@Data
public class Lazy {

    private static Lazy lazy;

    /**
     * 姓名
     */
    private String name;

    /**
     * 空构造
     */
    private Lazy (){

    }

    /**
     * 懒汉模式 没有才创建 线程安全
     * @return
     */
    public static Lazy getLazy (){
        if (null == lazy){
            synchronized (Lazy.class){
                if (null == lazy){
                    lazy = new Lazy();
                }
            }
        }
        return lazy;
    }

}
