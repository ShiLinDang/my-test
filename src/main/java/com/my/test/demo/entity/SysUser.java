package com.my.test.demo.entity;

import com.my.test.demo.annotation.SensitiveInfo;
import com.my.test.demo.constant.SensitiveType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:17
 */
@Data
@ToString
public class SysUser implements Serializable {
    private Long id;
    private String password;
    @SensitiveInfo(SensitiveType.CHINESE_NAME)
    private String realName;
    private String sex;
    @SensitiveInfo(SensitiveType.CHINESE_NAME)
    private String userName;
    private Integer userAge;
    private String mobilePhone;
}
