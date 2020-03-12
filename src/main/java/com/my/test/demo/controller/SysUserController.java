package com.my.test.demo.controller;

import com.my.test.demo.annotation.ApiIdempotent;
import com.my.test.demo.annotation.Sensitive;
import com.my.test.demo.constant.CommonConstant;
import com.my.test.demo.constant.MyTest;
import com.my.test.demo.lock.ExclusiveLock;
import com.my.test.demo.lock.ReadWriteLock;
import com.my.test.demo.mongoentity.Order;
import com.my.test.demo.entity.SysUser;
import com.my.test.demo.listener.MyHttpSessionListener;
import com.my.test.demo.service.*;
import com.my.test.demo.util.CodeUtil;
import com.my.test.demo.util.CuratorUtils;
import com.my.test.demo.util.JedisUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RLock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/22 11:35
 */
@RestController
@RequestMapping("user")
@Slf4j
public class SysUserController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedissonService redissonService;

    @Autowired
    private RedisUtilService redisUtilService;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private Environment environment;

    /**
     * zk集群地址
     */
    private static String connectString = "47.98.238.150:2181";

    @ApiIdempotent
    @PostMapping("/add")
    public void insert(@RequestBody SysUser user){
        userService.insert(user);
   }

   @GetMapping("/list")
   public String getList(){
        List<SysUser> userList = userService.getUserList();
       for (SysUser user : userList) {
           rabbitTemplate.convertAndSend("queue1-showUser",user);
           rabbitTemplate.convertAndSend("queue2-showName",user);
            try{
                Thread.sleep(2000L);
            }catch (InterruptedException e){
                e.printStackTrace();
                log.error(e.getMessage(),e);
            }
       }
        return userList.toString();
   }

   @GetMapping("/add-redis")
   public void addRedis(){
       List<SysUser> userList = userService.getUserList();
       String s = userList.toString();
       redisService.setValue("userList3:",s);
   }

   @GetMapping("/{userId}")
   public String getByUserId(@PathVariable Long userId){
        SysUser user = userService.getByUserId(userId);
       System.out.println("**************"+user.getRealName());
       return user.getRealName();
   }

    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("zxc", "zxc");
        return  "index";
    }

    @GetMapping("/token")
    public String getToken() {
        String str = String.valueOf(System.currentTimeMillis());
        StrBuilder token = new StrBuilder();
        token.append(str).append(":").append(new Random().nextInt(10000));
        String s = token.toString();
        jedisUtil.set(s,"token",3 * 60);
        return s;
    }


    @GetMapping("/online")
    public String online() {
        return  "当前在线人数：" + MyHttpSessionListener.online + "人";
    }

    @GetMapping("/get_name")
    public String getByName(@RequestParam String realName) {
        return realName;
    }

    @PostMapping("/order/add")
    public void addOrder(){
        try {
            Order order = new Order();
            order.setId(121L);
            order.setOrderNo(CodeUtil.getOrderNoCode("RO"));
            order.setAmount(new BigDecimal(100));
            order.setProductName("书籍");
            order.setState(0);
            order.setUserId(1L);
            orderService.save(order);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
    }

    /**
     * Jedis实现分布式锁
     */
    @PutMapping("/updateAge")
    public void updateUserAge(){
        Long id = 3L;
        String lockName = id.toString();
        String randomValue = String.valueOf(Thread.currentThread().getId());
        redisUtilService.acquireLock(lockName,randomValue,5_000);
        SysUser user = userService.findById(id);
        user.setUserAge(user.getUserAge()+1);
        userService.updateAge(user);
        redisUtilService.releaseLock(lockName,randomValue);
    }

    @PutMapping("/updateAgeByZoo")
    public void updateUserAgeByZoo(){
        Long id = 3L;
        CuratorFramework curatorFramework = CuratorUtils.getInstance(connectString);
        ExclusiveLock exclusiveLock = new ExclusiveLock(curatorFramework,"/user/age");
        exclusiveLock.getLock();
        SysUser user = userService.findById(id);
        user.setUserAge(user.getUserAge()+1);
        userService.updateAge(user);
        exclusiveLock.lockRelease();
        System.out.println("**************************");
    }

    /**
     * Redisson实现分布式锁
     */
    @PutMapping("/updateAge2")
    public void updateUserAge2(){
        Long id = 3L;
        String lockName = id.toString();
        RLock lock = redissonService.getLock(lockName);
        SysUser user = userService.findById(id);
        user.setUserAge(user.getUserAge()+1);
        userService.updateAge(user);
        lock.unlock();
    }

    @GetMapping("/test")
    public String showTest(){
        String testString = CommonConstant.TEST_STRING;
        CommonConstant constant = new MyTest();
        constant.show();
        CommonConstant.show2();
        String aliPay = CommonConstant.PaymentMethod.AliPay;
        return testString + ":" + CommonConstant.DeleteFlag.NORMAL.getDesc() + ":" + aliPay;
    }

    @GetMapping("/getAddress")
    public String getAddress(){
        // 获取资源配置
        String property = environment.getProperty("spring.redis.host");
        return property;
    }

    @GetMapping("/showSensitive")
    @Sensitive
    public SysUser showSensitive(){
        SysUser user = userService.findById(3L);
        return user;
    }
}
