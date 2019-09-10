package com.my.test.demo.tes;

import com.my.test.demo.impl.JavaDeveloper;
import com.my.test.demo.service.Developer;

import java.lang.reflect.Proxy;

/**
 * Description:动态代理测试
 *
 * @author dsl
 * @date 2019/9/10 11:04
 */
public class JavaDevelopProxy {
    public static void main(String[] args) {
        JavaDeveloper developer = new JavaDeveloper("dsl");
        Developer proxyInstance = (Developer)Proxy.newProxyInstance(developer.getClass().getClassLoader(), developer.getClass().getInterfaces(), (proxy, method, args1) -> {
            if (method.getName().equals("code")){
                System.out.println("dsl is praying for the code!");
                return null;
            }
            if (method.getName().equals("debug")){
                System.out.println("dsl is have no bug! No need to debug!");
                return null;
            }
            return null;
        });
        proxyInstance.code();
        proxyInstance.debug();
    }
}
