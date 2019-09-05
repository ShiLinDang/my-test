package com.my.test.demo.constant;

import lombok.Getter;

/**
 * Description: 常量类
 *
 * @author dsl
 * @date 2019/9/5 14:08
 */
public interface CommonConstant {

    /**
     * jdk8 interface 中常量定义默认被 static final 修饰
     */
    String TEST_STRING = "TEST";

    /**
     * 删除标记位
     */
    @Getter
    enum DeleteFlag{

        /**
         * 正常状态
         */
        NORMAL(0,"正常"),

        /**
         * 删除状态位
         */
        DELETE(99,"删除");

        /**
         * 标记编码
         */
        private Integer code;

        /**
         * 标记描述
         */
        private String desc;

        DeleteFlag (Integer code,String desc){
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 默认方法
     * @return
     */
    default String show(){
        return "Hahaha......";
    }

    /**
     * 静态方法
     * @return
     */
    static String show2(){
        return "Xixixix......";
    }

    /**
     * 支付方式
     */
    interface PaymentMethod {

        /**
         * BC 支付
         */
        String BCPay = "101";

        /**
         * 支付宝
         */
        String AliPay = "1001";

        /**
         * 微信支付
         */
        String WXPay = "1002";
    }
}
