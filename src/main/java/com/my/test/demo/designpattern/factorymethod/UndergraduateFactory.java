package com.my.test.demo.designpattern.factorymethod;

/**
 * Description:大学生工厂
 *
 * @author dsl
 * @date 2019/11/1 13:36
 */
public class UndergraduateFactory implements IFactory{
    @Override
    public LiuFeng createLiuFeng() {
        return new Undergraduate();
    }
}
