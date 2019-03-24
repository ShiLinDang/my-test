package com.my.test.demo.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * @author oufenglan
 * @create 2018-03-27 下午4:11
 * @desc 生成唯一编码的工具类
 **/


public class CodeUtil {


    /**默认分片*/
    private final static long   MODEL_NO       = 1024;

    /**8位数范围最小值*/
    public final static int    MIN_EIGHT        = 10000000;

    /**9位数范围最小值*/
    private final static int    MIN_NINE         = 100000000;


    /**8位数范围较大值*/
    public final static int    MAX_EIGHT        = 90000000;

    /**9位数范围较大值*/
    private final static int    MAX_NINE         = 900000000;


    /**8位数范围最小值*/
    private final static int    MIN_THREE       = 100;

    /**8位数范围较大值*/
    private final static int    MAX_THREE        = 900;

    /**
     * 11位数范围最小值
     */
    private final static long MIN_ELEVEN = 10_000_000_000L;
    /**
     * 11位数范围较大值
     */
    private final static long MAX_ELEVEN = 90_000_000_000L;

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private static Random RANDOM = new Random(System.currentTimeMillis());

    /**时间精确度*/
    //private static String SIGNATURE = String.valueOf(System.nanoTime());

    /**时间精确度*/
    private final static String PATTERN_MODEL = "yyyyMMddHHmmssSSS";

    private static String MESSAGE = "方法参数不能为空";

    /**
     *
     * 订单和预约的通用编码规则:  编码前缀（引用常量类：CodePrefixConstant） + yyyyMMddHHmmssSSS + 3位随机数 + 4位数模
     * 根据特征码与前缀生成id
     * @param prefix
     * @return
     */
    public static String getOrderNoCode(String prefix) {

        if(StringUtils.isNotBlank(prefix)){
            //yyyyMMddHHmmssSSS
            String time = DateUtil.getCurrentCompactDateTimeMorePreciseAsString();
            //3位随机数
            String random  = getRandom(MIN_THREE,MAX_THREE);
            //4位数模
            String model = getMod(String.valueOf(System.currentTimeMillis()));

            return buildCode(prefix,time,random) + model;
        }else{
            throw new RuntimeException(MESSAGE);
        }

    }

    /**
     * 生成唯一编号
     * @return
     */
    public static String getObjectNo(String string){
        String concat = string.concat(LocalDateTime.now().format(dtf).concat(new Random().nextInt(900) + 100 + ""));
        return concat;
    }


    /***
     * 生成ID(不带分片ID)
     * @param prefix
     * @return
     */
    private static String buildCode(String prefix, String time , String random) {


        StringBuffer strBuf = new StringBuffer();
        //前缀
        strBuf.append(prefix);
        //时间
        strBuf.append(time);
        //生成8位随机数
        strBuf.append(random);

        return strBuf.toString();
    }

    /**
     * JDK自带的Hash取模分片：将时间戳字符型的数据，唯一性地转为4位数值型
     *
     * @param signature
     * @return
     */
    private static String getMod(String signature) {

        CRC32 crc32 = new CRC32();
        crc32.update(String.valueOf(signature).getBytes());
        long model = crc32.getValue();
        //System.out.println("model:"+model);
        String mod = String.valueOf(model % MODEL_NO);
        //System.out.println("mod:"+mod);
        int len = mod.length();
        //System.out.println("len:"+len);
        for (int i = 0; i < 4 - len; i++) {
            mod = "0" + mod;
        }

        return mod;
    }



    /**
     * 取当前日期的年份后2位数，如20180327截取18
     * @return
     */
    public static String getDate() {

        //SimpleDateFormat+Date获取时间戳会存在并发问题
        String timestamp = DateUtil.getCurrentCompactDateTimeAsString();
        String time = timestamp.substring(2,4);

        return time;
    }


    public static String getRandom( int min,int max){

        //生成8位随机数
        Random num = new Random();
        String random = String.valueOf(num.nextInt(max)+min);

        return random;
    }

    /**
     * 编码+11位随机数字
     */
    public static String getRandomLong(String prefix) {
        return prefix + getRandom(MIN_ELEVEN, MAX_ELEVEN);
    }
    /**
     * 获取lang类型的随机数
     * @param min
     * @param max
     * @return String
     */
    public static String getRandom(Long min, Long max){
        Random num = new Random();
        long rangeLong = min + (((long) (num.nextDouble() * (max - min))));
        return String.valueOf(rangeLong);
    }

    /**
     * 通用订单code测试方法
     * @param pre
     */
    public static void testGetOrderNoCode(String pre){

        List list = new LinkedList();

        for (int i= 0; i < 10000; i++) {

            //测试订单编码方法是否唯一
            String order = getOrderNoCode(pre);
            //System.out.println(code);
            if (i > 0) {
                if(list.contains(order)){
                    System.out.println("有重复，order:"+order);
                }
            }

            list.add(order);

        }

        System.out.println("list长度:"+list.size());
    }



    public static void main(String[] args) {
        System.out.println(getRandomLong("qqqq"));
        System.out.println(String.valueOf(System.currentTimeMillis()));
        /*String pre = CodePrefixConstant.CodePrefixName.HOS_CHECK_ITEM;
        testGetCode(pre);*/
        //4位数模
        String aa = String.valueOf(System.currentTimeMillis());
        System.out.println("aa:"+aa);
        String model = getMod(aa);
        System.out.println("model:"+model);
    }
}

