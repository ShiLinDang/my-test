package com.my.test.demo.service;

import com.my.test.demo.entity.SysUser;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:39
 */
public interface SysUserService {
    void insert(SysUser user);

    List<SysUser> getUserList();
}
