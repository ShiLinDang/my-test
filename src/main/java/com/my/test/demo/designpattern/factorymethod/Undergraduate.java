package com.my.test.demo.designpattern.factorymethod;

/**
 * Description:大学生类
 *
 * @author dsl
 * @date 2019/11/1 13:39
 */
public class Undergraduate extends LiuFeng{
    public Undergraduate(){
        setWhichOne(this.getClass().getSimpleName());
    }
}
