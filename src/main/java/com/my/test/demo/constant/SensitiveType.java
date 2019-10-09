package com.my.test.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public enum SensitiveType {

    /**
     * 中文名 (张三 → 张*)
     */
    CHINESE_NAME("(?<=.{1}).", "*"),

    /**
     * 密码
     */
    PASSWORD(".", ""),

    /**
     * 身份证号
     */
    ID_CARD("(?<=\\w{3})\\w(?=\\w{4})", "*"),

    /**
     * 座机号
     */
    FIXED_PHONE("(?<=\\w{3})\\w(?=\\w{2})", "*"),

    /**
     * 手机号
     */
    MOBILE_PHONE("(?<=\\w{3})\\w(?=\\w{4})", "*"),

    /**
     * 地址
     */
    ADDRESS("(.{5}).+(.{4})", "$1*****$2"),

    /**
     * 电子邮件
     */
    EMAIL("(\\w+)\\w{3}@(\\w+)", "$1***@$2"),

    /**
     * 银行卡
     */
    BANK_CARD("(?<=\\w{4})\\w(?=\\w{4})", "*"),

    /**
     * 公司开户银行联号
     */
    CNAPS_CODE("(?<=\\w{4})\\w(?=\\w{4})", "*");

    @Getter
    @Setter
    private String pattern;//匹配格式

    @Getter
    @Setter
    private String targetChar;//替代的字符
}
