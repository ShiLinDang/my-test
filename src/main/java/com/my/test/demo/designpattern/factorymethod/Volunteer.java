package com.my.test.demo.designpattern.factorymethod;

/**
 * Description:志愿者
 *
 * @author dsl
 * @date 2019/11/1 13:49
 */
public class Volunteer extends LiuFeng{
    public Volunteer (){
        // 调用父类构造函数构造LeiFeng对象,同时运用了多态
        super();
        // this Volunteer 谁调用指代谁
        setWhichOne(this.getClass().getSimpleName());
    }
}
