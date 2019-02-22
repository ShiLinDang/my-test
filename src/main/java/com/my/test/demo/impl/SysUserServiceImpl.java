package com.my.test.demo.impl;

import com.my.test.demo.dao.SysUserDao;
import com.my.test.demo.entity.SysUser;
import com.my.test.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:40
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao userDao;

    @Override
    public void insert(SysUser user) {
        userDao.insert(user);
    }

    @Override
    public List<SysUser> getUserList() {
        return userDao.getUserList();
    }
}
