package com.my.test.demo.runner;

import com.my.test.demo.SysUserApp;
import com.my.test.demo.entity.SysUser;
import com.my.test.demo.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/3/710:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SysUserApp.class)
public class SysUserTest{

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void getList(){
        List<SysUser> userList = sysUserService.getUserList();
        System.out.println("userList:" + userList.toString());
    }
}
