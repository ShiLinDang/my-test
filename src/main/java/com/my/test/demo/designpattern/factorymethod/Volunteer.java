package com.my.test.demo.designpattern.factorymethod;

/**
 * Description:志愿者
 *
 * @author dsl
 * @date 2019/11/1 13:49
 */
public class Volunteer extends LiuFeng{
    public Volunteer (){
        setWhichOne(this.getClass().getSimpleName());
    }
}
