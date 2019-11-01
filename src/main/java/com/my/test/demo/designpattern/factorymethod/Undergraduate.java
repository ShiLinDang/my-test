package com.my.test.demo.designpattern.factorymethod;

/**
 * Description:大学生类
 *
 * @author dsl
 * @date 2019/11/1 13:39
 */
public class Undergraduate extends LiuFeng{
    public Undergraduate(){
        // 调用父类构造函数构造LeiFeng对象,同时运用了多态
        super();
        // this 指代Undergraduate 谁调用指代谁
        setWhichOne(this.getClass().getSimpleName());
    }
}
