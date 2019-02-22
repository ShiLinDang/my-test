package com.my.test.demo.dao;

import com.my.test.demo.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:41
 */
@Repository
public interface SysUserDao {
    void insert(SysUser user);

    List<SysUser> getUserList();
}
