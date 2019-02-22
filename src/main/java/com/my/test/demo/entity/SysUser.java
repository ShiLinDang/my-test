package com.my.test.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:17
 */
@Data
@ToString
public class SysUser implements Serializable {
    @Id
    private Long id;
    private String password;
    private String realName;
    private String sex;
    private String userName;
    private Integer userAge;
    private String mobilePhone;
}
