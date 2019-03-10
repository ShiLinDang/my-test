package com.my.test.demo.controller;

import com.my.test.demo.entity.SysUser;
import com.my.test.demo.listener.MyHttpSessionListener;
import com.my.test.demo.service.RedisService;
import com.my.test.demo.service.SysUserService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private RedisService redisService;

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

   @GetMapping("/add-redis")
   public void addRedis(){
       List<SysUser> userList = userService.getUserList();
       String s = userList.toString();
       redisService.setValue("userList3:",s);
   }

    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("zxc", "zxc");
        return  "index";
    }

    @GetMapping("/online")
    public String online() {
        return  "当前在线人数：" + MyHttpSessionListener.online + "人";
    }

    @GetMapping("/get_name")
    public String getByName(@RequestParam String realName) {
        return realName;
    }
}
