package com.my.test.demo.designpattern.factorymethod;

/**
 * Description:志愿者工厂
 *
 * @author dsl
 * @date 2019/11/1 13:54
 */
public class VolunteerFactory implements IFactory{
    @Override
    public LiuFeng createLiuFeng() {
        // Volunteer 调用自己的构造函数,在自己的构造函数内通过反射调用创建对象
        return new Volunteer();
    }

    public static void main(String[] args) {
        VolunteerFactory factory = new VolunteerFactory();
        LiuFeng liuFeng = factory.createLiuFeng();
    }
}
