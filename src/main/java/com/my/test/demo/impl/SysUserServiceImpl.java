package com.my.test.demo.impl;

import com.my.test.demo.dao.SysUserDao;
import com.my.test.demo.entity.SysUser;
import com.my.test.demo.service.RedisUtilService;
import com.my.test.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private RedisUtilService redisUtilService;

    @Override
    public void insert(SysUser user) {
        String lockName = user.getMobilePhone();
        String randomValue = String.valueOf(Thread.currentThread().getId());
        redisUtilService.acquireLock(lockName,randomValue,5_000);
        userDao.insert(user);
        redisUtilService.releaseLock(lockName,randomValue);
    }

    @Override
    public List<SysUser> getUserList() {
        return userDao.getUserList();
    }

    @Override
    public void updateAge(SysUser user) {
        userDao.updateAge(user);
    }

    @Override
    public SysUser findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    @Cacheable(cacheNames = "getOne",key = "#userId.toString()",condition = "#userId==3")
    public SysUser getByUserId(Long userId) {
        SysUser user = userDao.findById(userId);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        return user;
    }
}
