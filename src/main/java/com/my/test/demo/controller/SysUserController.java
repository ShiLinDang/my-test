package com.my.test.demo.controller;

import com.my.test.demo.entity.SysUser;
import com.my.test.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2211:35
 */
@RestController
@RequestMapping("user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/add")
    public void insert(@RequestBody SysUser user){
        userService.insert(user);
   }

   @GetMapping("/list")
   public String getList(){
        List<SysUser> userList = userService.getUserList();
        return userList.toString();
   }
}
