package com.my.test.demo.service;

import com.my.test.demo.entity.SysUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:39
 */
@Transactional
public interface SysUserService {
    void insert(SysUser user);

    List<SysUser> getUserList();

    void updateAge(SysUser user);

    SysUser findById(Long id);
}
