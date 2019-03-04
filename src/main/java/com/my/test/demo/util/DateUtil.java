package com.my.test.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * @author oufenglan
 * @create 2018-03-27 下午6:53
 * @desc 时间各种格式转换的工具类
 **/

public class DateUtil {

    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String DATE_FORMAT_PATTERN_CN = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN_CN_M = "yyyy年MM月dd日 HH点mm分";
    public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String APP_DATE_FORMAT_PATTERN ="yyyy/MM/dd HH:mm:ss";
    public static final String APP_MM_DATE_FORMAT_PATTERN ="yyyy/MM/dd HH:mm";
    public static final String DATETIME_SSS_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**时间精确度*/
    private final static String PATTERN_MODEL = "yyyyMMddHHmmssSSS";

    public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";
    public static final String COMPACT_DATETIME_FORMAT_PATTERN = "yyyyMMddHHmmss";
    public static final Integer MONTH_DAYS = 30;
    public static final Integer LONGEST_MONTH_DAYS = 31;
    public static final Integer YEAR_DAYS = 365;
    public static final Integer YEAR_MONTHS = 12;
    public static final Integer  MID_TIME_DECIMAL_DIGIT = 10;
    public static final int ACHIEVE_DAY = 7;
    public static final int ACHIEVE_HOUR = 10;
    public static final int ACHIEVE_MIN = 0;
    public static final String ACHIEVE_MONTH_PARSE = "yyyy-MM";

    /**
     * 年月日格式
     */
    public static final String YEARMONTHDAY_FORMAT = "yyyyMMdd";
    /**
     * 日期调整 默认为天
     *
     * @param adj
     * @return
     */
    public static final String dateAdjust(String date, int adj) {
        return dateAdjust(date, adj, Calendar.DAY_OF_YEAR);
    }

    public static List<Date> parseDateList(List<String> dateStrings, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        List<Date> dateList = new ArrayList<>();
        for (String dateString : dateStrings) {
            try {
                dateList.add(simpleDateFormat.parse(dateString));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return dateList;
    }

    public static List<String> formatDateList(List<Date> dates, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        List<String> dateList = new ArrayList<>();
        for (Date date : dates) {
            dateList.add(simpleDateFormat.format(date));
        }
        return dateList;
    }

    /**
     * 日期调整
     *
     * @param date     日期格式 yyyy-MM-dd
     * @param adj      日期调整 正数 往后，负数往前
     * @param timeUnit 为时间单位 天、周、月、年
     * @return
     */
    public static final String dateAdjust(String date, int adj, int timeUnit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.parse(date));
        cal.add(timeUnit, adj);
        return DateUtil.format(cal.getTime());
    }

    /**
     * 解析字符串成java.util.Date 使用yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static Date parse(String str) {
        String pattern = DATE_FORMAT_PATTERN;
        if(str!=null && str.length()>=21){
            pattern = DATETIME_SSS_FORMAT_PATTERN;
        }else if(str!=null && str.length()==19){
            pattern = DATETIME_FORMAT_PATTERN;
        }
        return parse(str, pattern);
    }
    /**
     *
     * @Description:先对str进行10位
     * @param str
     * @return
     * Date
     * @exception:
     * @author: zhangqy
     * @time:2016年7月14日 上午12:14:32
     */
    public static Date parseNew(String str) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return parse(str.substring(0,10), DATE_FORMAT_PATTERN);
    }

    /**
     * 解析字符串成java.util.Date 使用pattern
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Date parse(String str, String pattern) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return DateUtils.parseDate(str, pattern);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't parse " + str + " using " + pattern);
        }
    }


    public static double diffMonth(Date startDate,Date endDate){
//        if(startDate.getTime()<=endDate.getTime()){
//            return doDiffMonth(startDate,endDate);
//        }else{
//            return -doDiffMonth(endDate,startDate);
//        }
        return getTimeWithMonthDays(startDate, endDate).doubleValue();
    }

    /**
     * 跟业绩一样的算法
     *
     * @author yujinjun
     * @param beginTime
     * @param endTime
     * @return
     * @exception
     * @since JDK 1.7
     */
    public static BigDecimal getTimeWithMonthDays(Date beginTime, Date endTime){
        if(beginTime==null || endTime == null){
            return new BigDecimal(0);
        }
//		if(beginTime.after(endTime)){ //|| beginTime.equals(endTime)){
//			return new  BigDecimal(0);
//		}
        //获取月份
        int months=getMonths(beginTime, endTime);
        int days= getDays(beginTime, endTime,months);
        CycleEndDate ced = new CycleEndDate(beginTime,months+1);
        if(ced.getEndDate().equals(endTime)){
            days=0;
            months++;
        }
        BigDecimal bd =new BigDecimal(months);
        return bd.add(new BigDecimal(days).multiply(new BigDecimal(1).
                                                                             multiply(new BigDecimal(YEAR_MONTHS)).
                                                                             divide(new BigDecimal(YEAR_DAYS),MID_TIME_DECIMAL_DIGIT, BigDecimal.ROUND_HALF_UP)));
    }

    private static Integer getMonths(Date beginTime,Date endTime){
        int i=0;
        Date beginDate=beginTime;
        Calendar car = Calendar.getInstance();
        car.setTime(beginTime);
        int days=car.get(Calendar.DAY_OF_MONTH);
        boolean flag= days==1;
        do{
            CycleEndDate ced = new CycleEndDate(beginDate,1);
            Date endDate =ced.getEndDate();
            if(!flag){
                endDate=juestEndDate(endDate,days-1);
            }
            beginDate = DateUtil.addDays(endDate,1);
            i++;
        }while(beginDate.before(endTime)||beginDate.equals(endTime));
        i--;
        return i;
    }

    private static Date juestEndDate(Date endDate,Integer endDay){
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.set(Calendar.DAY_OF_MONTH, endDay);
        if(cal.getTime().before(endDate)){
            return cal.getTime();
        }else{
            return endDate;
        }
    }

    private static Integer getDays(Date beginTime,Date endTime,Integer month){

        Integer days=0;
        if(month>0){
            CycleEndDate ced = new CycleEndDate(beginTime,month);
            days =DateUtil.daysBetween(DateUtil.addDays(ced.getEndDate(), 1), endTime);
        }else{
            days =DateUtil.daysBetween(beginTime, endTime);
        }
        //+1是时间间隔
        return days>=0?days+1:days-1;

//		return days =(days>ContractParam.MONTH_DAYS
//					&&	days<=ContractParam.LONGEST_MONTH_DAYS)?ContractParam.MONTH_DAYS:days+1;
    }

    @Deprecated
    private static double doDiffMonth(Date startDate,Date endDate){
        Calendar cal=Calendar.getInstance();
        cal.setTime(startDate);

        Calendar cal1=Calendar.getInstance();
        cal1.setTime(endDate);

        if(cal1.get(Calendar.YEAR)==cal.get(Calendar.YEAR) && cal1.get(Calendar.MONTH)==cal.get(Calendar.MONTH)){
            return (new BigDecimal(cal1.get(Calendar.DATE)-cal.get(Calendar.DATE)).divide(new BigDecimal(30.41667),5,ROUND_DOWN).doubleValue());
        }

        int months=((cal1.get(Calendar.YEAR)*12+cal1.get(Calendar.MONTH))-(cal.get(Calendar.YEAR)*12+cal.get(Calendar.MONTH)))-1;

        int startDay=cal.get(Calendar.DATE);

        cal.add(Calendar.MONTH,1);
        cal.setTime(parse(format(cal.getTime(),"yyyy-MM-dd")));
        cal.add(Calendar.DATE,-cal.get(Calendar.DATE));

        int startDay1=cal.get(Calendar.DATE);

        int day2=(startDay1-startDay+cal1.get(Calendar.DATE));

        return new BigDecimal(day2).divide(new BigDecimal(30.41667),5,ROUND_DOWN).add(new BigDecimal(months)).doubleValue();

    }

    /**
     * 根据时间变量返回时间字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 按<code>yyyy-MM-dd</code>格式化日期成字符串
     */
    public static String format(Date date) {
        return format(date, DATE_FORMAT_PATTERN);
    }


    /**
     * 按<code>yyyy-MM-dd</code>格式化日期成字符串
     */
    public static String formatCn(Date date) {

        return format(date, DATE_FORMAT_PATTERN_CN);
    }

    /**
     * 取当前系统时间【HH:mm:ss】
     */
    public static String getCurrentTimeAsString() {
        return format(new Date(), TIME_FORMAT_PATTERN);
    }

    public static String getCurrentTimeAsString(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 返回当前日期，按<code>yyyyMMddHHmmss</code>格式化日期成字符串
     */
    public static String getCurrentCompactDateTimeAsString() {
        return format(new Date(), COMPACT_DATETIME_FORMAT_PATTERN);
    }

    /**
     * 返回当前日期，按<code>yyyyMMddHHmmssSSS</code>格式化日期成字符串
     */
    public static String getCurrentCompactDateTimeMorePreciseAsString() {
        return format(new Date(), PATTERN_MODEL);
    }

    public static void main(String[] args) {
        Date date = parse("2018-12-13 14:11:04", DATETIME_FORMAT_PATTERN);
        System.out.println(formatCn(date));
    }

    /**
     * 返回当前日期，按<code>yyyy-MM-dd</code>格式化日期成字符串
     */
    public static String getCurrentDateAsString() {
        return format(new Date(), DATE_FORMAT_PATTERN);
    }

    /**
     * 返回按照当前日期，按<code>pattern</code>格式化的字符串
     */
    public static String getCurrentDateAsString(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 返回当前日期，按 <code>yyyy-MM-dd HH:mm:ss</code>格式化成字符串
     */
    public static String getCurrentDateTimeAsString() {
        return format(new Date(), DATETIME_FORMAT_PATTERN);
    }

    /**
     * 返回当前日期，按 <code>yyyy-MM-dd HH:mm:ss</code>格式化成字符串
     */
    public static String getCurrentDateTimeAsString(int field, int delta) {
        Calendar cal = Calendar.getInstance();
        cal.add(field, delta);
        Date date = cal.getTime();
        return format(date, DATETIME_FORMAT_PATTERN);
    }

    /**
     * 返回当前日期所在年的第一天
     */
    public static Date getStartDateTimeOfCurrentYear(){
        return getStartDateTimeOfYear(new Date());
    }

    /**
     * 返回指定日期所在年的第一天
     * @param date
     * @return Date
     */
    public static Date getStartDateTimeOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回当前日期所在月份的第一天
     *
     * @return
     */
    public static Date getStartDateTimeOfCurrentMonth() {
        return getStartDateTimeOfMonth(new Date());
    }

    /**
     * 返回指定日期所在月份的第一天 The value of
     * <ul>
     * <li>Calendar.HOUR_OF_DAY
     * <li>Calendar.MINUTE
     * <li>Calendar.MINUTE
     * </ul>
     * will be set 0.
     */
    public static Date getStartDateTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回当前日期所在月份的最后一天
     *
     * @return
     */
    public static Date getEndDateTimeOfCurrentMonth() {
        return getEndDateTimeOfMonth(new Date());
    }

    /**
     * 返回指定日期所在月份的最后一天
     *
     * @param date
     * @return
     */
    public static Date getEndDateTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    /**
     * 返回当前日期所在周的第一天
     *
     * @return
     */
    public static Date getStartDateTimeOfCurrentWeek() {
        return getStartDateTimeOfWeek(new Date());
    }

    /**
     * 返回指定日期所在周的第一天 The value of
     * <ul>
     * <li>Calendar.HOUR_OF_DAY
     * <li>Calendar.MINUTE
     * <li>Calendar.MINUTE
     * </ul>
     * will be set 0.
     */
    public static Date getStartDateTimeOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回当天的凌晨时间
     *
     * @return
     */
    public static Date getStartTimeOfCurrentDate() {
        return getStartTimeOfDate(new Date());
    }

    /**
     * 返回指定日期的凌晨时间
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 返回当天的结束时间 <tt>2005-12-27 17:58:56</tt> will be returned as
     * <tt>2005-12-27 23:59:59</tt>
     */
    public static Date getEndTimeOfCurrentDate() {
        return getEndTimeOfDate(new Date());
    }

    /**
     * 返回指定日期的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndTimeOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    // /**
    // * 在指定时间基础上增加 指定小时
    // * @param date
    // * @param hours
    // * @return
    // */
    // public static Date addHours(Date date, int hours) {
    // return add(date, Calendar.HOUR_OF_DAY, hours);
    // }
    // /**
    // * 在指定时间基础上增加 指定分钟
    // * @param date
    // * @param minutes
    // * @return
    // */
    // public static Date addMinutes(Date date, int minutes) {
    // return add(date, Calendar.MINUTE, minutes);
    // }
    //
    // /**
    // * 在指定时间基础上增加 指定天数
    // * @param date
    // * @param days
    // * @return
    // */
    // public static Date addDays(Date date, int days) {
    // return add(date, Calendar.DATE, days);
    // }
    //
    // /**
    // * 在指定时间基础上增加 指定月份
    // * @param date
    // * @param months
    // * @return
    // */
    // public static Date addMonths(Date date, int months) {
    // return add(date, Calendar.MONTH, months);
    // }
    //
    // /**
    // * 在指定时间基础上增加 指定年份
    // * @param date
    // * @param years
    // * @return
    // */
    // public static Date addYears(Date date, int years) {
    // return add(date, Calendar.YEAR, years);
    // }

    // private static Date add(Date date, int field, int amount) {
    // Calendar cal = Calendar.getInstance();
    // cal.setTime(date);
    // cal.add(field, amount);
    // return cal.getTime();
    // }

    /**
     * 计算两个日期之间的天数
     */
    public static final int daysBetween(Date early, Date late) {
        Calendar ecal = Calendar.getInstance();
        Calendar lcal = Calendar.getInstance();
        ecal.setTime(early);
        lcal.setTime(late);

        long etime = ecal.getTimeInMillis();
        long ltime = lcal.getTimeInMillis();

        return (int) ((ltime - etime) / MILLIS_PER_DAY);
    }

    /**
     * 计算两个日期之间的天数
     */
    public static final int daysBetween(String early, String late) {
        Date dateEarly = parse(early);
        Date dateLate = parse(late);
        return daysBetween(dateEarly, dateLate);
    }


    /**
     * yyyyMMdd yyyy-MM-dd互相转换
     *
     * @param yyyy_mm_dd
     * @return
     */
    public static final String toDate8Char(String yyyy_mm_dd) {
        return StringUtils.replace(yyyy_mm_dd, "-", "");
    }

    public static final String toDate10Char(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.length() < 8) {
            return yyyymmdd;
        } else {
            return yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6) + "-" + yyyymmdd.substring(6, 8);
        }
    }

    /**
     * 在指定时间基础上增加 指定小时
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    /**
     * 在指定时间基础上增加 指定分钟
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        return add(date, Calendar.MINUTE, minutes);
    }

    /**
     * 在指定时间基础上增加 指定天数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return add(date, Calendar.DATE, days);
    }


    /**
     * 在指定时间基础上增加 指定月份
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        return add(date, Calendar.MONTH, months);
    }

    /**
     * 在指定时间基础上增加 指定年份
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        return add(date, Calendar.YEAR, years);
    }

    private static Date add(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

	/*
     * public static String format(Date date) { return format(date,
	 * "yyyy-MM-dd"); }
	 *
	 * public static String format(Date date, String mask) { SimpleDateFormat fm
	 * = new SimpleDateFormat(mask); return fm.format(date); }
	 *
	 * public static Date parse(String date) { return parse(date, "yyyy-MM-dd");
	 * }
	 *
	 * public static Date parse(String str, String mask) { SimpleDateFormat fm =
	 * new SimpleDateFormat(mask); try { return fm.parse(str); } catch
	 * (ParseException e) { throw new IllegalArgumentException(e); } }
	 */

    /**
     * 返回年份
     *
     * @return int
     */
    public static int getNowYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 返回月份
     *
     * @return int
     */
    public static int getNowMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回天
     *
     * @param now
     * @return int
     */
    public static int getDaysOfMonth(Date now) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(now);
        return calender.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回天
     *
     * @return int
     */
    public static int getNowDayOfMonth() {
        return getDaysOfMonth(new Date());
    }

    /**
     * 返回小时
     *
     * @return int
     */
    public static int getNowHourOfDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        String ret = "";
        if (date == null) {
            return ret;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ret = formatter.format(date);
        return ret;
    }

    /**
     * @return
     */
    public static String dateStrToyyyyMMddHHmm(String dateStr) {
        if (null != dateStr && dateStr.length() > 15) {
            return dateStr.substring(0, 16);
        }
        return dateStr;
    }

    /**
     * @param dateStr
     * @return
     */
    public static String dateStrToyyyyMMddHHmmss(String dateStr) {
        if (null != dateStr && dateStr.length() >= 19) {
            return dateStr.substring(0, 19);
        }
        return dateStr;
    }

    /**
     * 获取两个时间相差天数【在租金计算中使用，修改的时候请注意】
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long diffValue(Date date1, Date date2) {
        if (date2.before(date1)) {
            return (date1.getTime() - date2.getTime()) / 86400000 + 1;
        } else {
            return (date2.getTime() - date1.getTime()) / 86400000 + 1;
        }
    }

    /**
     * 获取月份的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 日期
     */
    public static Integer getEndDateTimeOfMonth(int year, int month) {
        return getActualMaximumNew(year,month);
    }

    public static Integer getActualMaximumNew(int year,int month){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMM");
        Date dd=null;
        try {
            dd = sdf0.parse(year+""+month);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.setTime(dd);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取明天
     *
     * @param resDate 今天
     * @return
     */
    public static Date nextDay(Date resDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resDate);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 免租期开始时间
     */
    public static Date createFeeBeginDate(Date beginDate, Date endDate, Date feeBeginDate, Date feeEndDate) {
        Calendar bcalendar = Calendar.getInstance();
        Calendar ecalendar = Calendar.getInstance();

        bcalendar.setTime(beginDate);
        ecalendar.setTime(endDate);
        int byear = bcalendar.get(Calendar.YEAR);
        int eyear = ecalendar.get(Calendar.YEAR);
        if (byear == eyear) {
            //如果开始时间和结束时间在同一年
            return getFeeBeginDate(beginDate, endDate, byear, feeBeginDate, feeEndDate);
        } else {
            Date d = getFeeBeginDate(beginDate, endDate, byear, feeBeginDate, feeEndDate);
            if (d == null) {
                return getFeeBeginDate(beginDate, endDate, eyear, feeBeginDate, feeEndDate);
            } else {
                return d;
            }
        }
    }

    /**
     * 免租期结束时间
     */
    public static Date createFeeEndDate(Date beginDate, Date endDate, Date feeBeginDate, Date feeEndDate) {
        Calendar bcalendar = Calendar.getInstance();
        Calendar ecalendar = Calendar.getInstance();

        bcalendar.setTime(beginDate);
        ecalendar.setTime(endDate);
        int byear = bcalendar.get(Calendar.YEAR);
        int eyear = ecalendar.get(Calendar.YEAR);
        if (byear == eyear) {
            //如果开始时间和结束时间在同一年
            return getFeeEndDate(beginDate, endDate, byear, feeBeginDate, feeEndDate);
        } else {
            Date d = getFeeEndDate(beginDate, endDate, byear, feeBeginDate, feeEndDate);
            if (d == null) {
                return getFeeEndDate(beginDate, endDate, eyear, feeBeginDate, feeEndDate);
            } else {
                return d;
            }
        }
    }

    /*
     * 比较免租期开始时间
     */
    public static Date getFeeBeginDate(Date beginDate, Date endDate, int year, Date feeBeginDate, Date feeEndDate) {
        Calendar calendar = Calendar.getInstance();
        //Calendar calendar1 = Calendar.getInstance();

        calendar.setTime(feeBeginDate);
        calendar.set(Calendar.YEAR, year);
//        calendar1.setTime(feeEndDate);
//        calendar1.set(Calendar.YEAR, year);

        if ((calendar.getTime().after(beginDate) || calendar.getTime().equals(beginDate))
            && (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate))) {
            return calendar.getTime();
        }
//        else if ((calendar1.getTime().after(beginDate) || calendar1.getTime().equals(beginDate))
//                && (calendar1.getTime().before(endDate) || calendar1.getTime().equals(endDate))) {
//            return calendar.getTime();
//        }
        else {
            return null;
        }
    }

    /**
     * 获取免租期结束时间
     */
    public static Date getFeeEndDate(Date beginDate, Date endDate, int year, Date feeBeginDate, Date feeEndDate) {
        Calendar calendar = Calendar.getInstance();
//        Calendar calendar1 = Calendar.getInstance();
//        if(endDate.equals(feeEndDate)){
//        	return feeEndDate;
//        }
        calendar.setTime(feeEndDate);
        calendar.set(Calendar.YEAR, year);
//        calendar1.setTime(feeEndDate);
//        calendar1.set(Calendar.YEAR, year);
        if ((calendar.getTime().after(beginDate) || calendar.getTime().equals(beginDate))
            && (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate))) {
            return calendar.getTime();
        }
//        else if ((calendar1.getTime().after(beginDate) ||calendar1.getTime().equals(beginDate))
//                && (calendar1.getTime().before(endDate) || calendar1.getTime().equals(endDate))) {
//            return calendar1.getTime();
//        }
        else {
            return null;
        }
    }

    /**
     * 计算月份差，date1要小于date2
     *
     * @param date1
     * @param date2
     * @return int
     */
    public static int getMonthNum(String date1, String date2) {
        if (StringUtils.isEmpty(date1) || date1.length() < 10 || StringUtils.isEmpty(date2) || date2.length() < 10) {
            return 0;
        }
        String[] strArra1 = date1.split("-");
        String[] strArra2 = date2.split("-");
        int day1 = Integer.valueOf(strArra1[2]);
        int day2 = Integer.valueOf(strArra2[2]);
        int dayNum = day1 - day2;
        int day = 0;
        if (dayNum < 0) {
            day = 1;
        }
        return Math.abs(Integer.valueOf(strArra1[0]) * 12 - Integer.valueOf(strArra2[0]) * 12 + Integer.valueOf(strArra1[1]) - Integer.valueOf(strArra2[1])) + day;
    }
    /**
     *
     * @Description:判断时间是否在某个时间段内
     * @param bdate
     * @param edate
     * @param date
     * @return
     * boolean
     * @exception:
     * @author: zhangqy
     * @time:2016年6月23日 上午10:33:01
     */
    public static boolean isbetween(Date bdate,Date edate,Date date){
        if((date.after(bdate)||date.equals(bdate))&&(date.before(edate)||date.equals(edate))){
            return true;
        }
        return false;
    }

    /**
     * 将时间日期从str变成date
     * @param dateStr
     * @return
     */
    public static Date strToDate(String dateStr) {
        if (dateStr==null||"".equals(dateStr.trim())) {
            return null;
        }
        try {
            return strToDate(dateStr, DATE_FORMAT_PATTERN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date strToDate(String dateStr, String format) {
        Date ret = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            ret  = formatter.parse(dateStr);
        } catch (ParseException e) {
        }
        return ret;
    }




    /**
     * 获取昨天
     *
     * @param resDate 今天
     * @return
     */
    public static Date yesterDay(Date resDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(resDate);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static int getMonthDif(Date start, Date end) {
        Date index = start;
        int count = 0;
        while (true) {
            if(DateUtil.addMonths(index, 1).compareTo(DateUtil.addDays(end, 1)) < 0) {
                index = DateUtil.addMonths(index, 1);
                if(DateUtil.dateToString(index, "yyyy-MM-dd").endsWith("02-29") || DateUtil.dateToString(index, "yyyy-MM-dd").endsWith("02-28")){
                    index = DateUtil.addDays(index, 1);
                    continue;
                }
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    public static Date formatDate(Date date, String formatStr) {
        Date ret = null;
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        String dateStr = formatter.format(date);
        try {
            ret = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }
    /**
     * 把格林时间转本地时间
     * @param date
     * @return
     */
    public static String formatGMLData(String date){
        if(date.indexOf("Z") !=-1){
            String  dateStr = date.replace("Z", " UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            try {
                Date dt = sdf.parse(dateStr);
                SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
                dateStr= format.format(dt);
                return 	dateStr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }


    /**
     * 判断时间七天内的时间
     * @param date
     * @return
     */
    public static boolean isLatestWeek(String date){
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, 7);  //设置为7天前
        Date after7time = calendar.getTime();   //得到7天前的时间
        Date addDate = parse(date,DATETIME_FORMAT_PATTERN);
        if(addDate.getTime() > after7time.getTime()){
            return true;
        }
        return false;
    }

    /**
     *
     * 判断时间大于当前时间两小时、
     * @param date
     * @return
     */
    public static boolean isBefer2Hours(String date){
        Calendar cal = Calendar.getInstance();  //得到日历
        cal.setTime(new Date());//把当前时间赋给日历
        cal.add(Calendar.HOUR, 2);
        Date after2Hour = cal.getTime(); //等到两小时后的时间
        Date addDate = parse(date,DATETIME_FORMAT_PATTERN);
        if(addDate.getTime() < after2Hour.getTime()){
            return true;
        }
        return false;
    }

    public static class CycleEndDate implements Serializable {

        private static final long serialVersionUID = -787281002446288635L;
        /** 周期开始时间 **/
        public Date startDate;
        /** 周期数量 **/
        public Integer cycles;
        /** 周期结束时间 计算得到 **/
        public Date endDate;

        public CycleEndDate(Date startDate, Integer cycles){

            if(startDate == null || cycles == null || cycles <= 0){
                this.endDate = null;
            } else {
                this.startDate = DateUtil.parse(DateUtil.format(startDate));
                this.cycles = cycles;
                //计算周期的结束日期
                this.calculateEndDate();
            }
        }

        private void calculateEndDate(){
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            String[] startDateArray = ft.format(this.startDate).split("-");

            int yearRes = 0;
            int monthRes = 0;
            int dayRes = 0;

            //如果签约的日期的 天-1 大于0，其实就是，签约日期不是1号
            if((Integer.parseInt(startDateArray[2]) - 1) > 0){
                //如果签约的日期的月份加计算的第几周期 大于12，是在同一年。否则为下一年
                if((Integer.parseInt(startDateArray[1]) + this.cycles) <= 12){

                    //周期到期日的年份为当前年份
                    yearRes = Integer.parseInt(startDateArray[0]);
                    //周期到期日的月份份为委托开始日期的月份加上周期
                    monthRes = Integer.parseInt(startDateArray[1]) + this.cycles;

                    //当前计算周期的月的 最后一天 比如计算 2015-06 的第1期 那么endDay为2015-07最后一天
                    int endDay = DateUtil.getEndDateTimeOfMonth(yearRes, monthRes);

                    //如果签约日期的天-1 大于 endDay 那么 签约日期的天-1不存在。以endDay为到期日。反之签约日期的天-1为到期日
                    if((Integer.parseInt(startDateArray[2]) - 1) > endDay) {
                        dayRes = endDay;
                    } else if((Integer.parseInt(startDateArray[2]) - 1) <= endDay){
                        dayRes = Integer.parseInt(startDateArray[2]) - 1;
                    }
                } else {

                    //年份为当前年份
                    yearRes = Integer.parseInt(startDateArray[0]) + 1;
                    //周期到期日的月份份为委托开始日期的月份加上周期
                    monthRes = Integer.parseInt(startDateArray[1]) + this.cycles -12;

                    //当前计算周期的月的 最后一天 比如计算 2015-06 的第1期 那么endDay为2015-07最后一天
                    int endDay = DateUtil.getEndDateTimeOfMonth(yearRes, monthRes);

                    //如果签约日期的天-1 大于 endDay 那么 签约日期的天-1不存在。以endDay为到期日。反之签约日期的天-1为到期日
                    if((Integer.parseInt(startDateArray[2]) - 1) > endDay) {
                        dayRes = endDay;
                    } else if((Integer.parseInt(startDateArray[2]) - 1) <= endDay){
                        dayRes = Integer.parseInt(startDateArray[2]) - 1;
                    }
                }
            } else {

                //如果签约的日期为1号的情况

                if((Integer.parseInt(startDateArray[1]) + this.cycles) <= 12){
                    //周期到期日的年份为当前年份
                    yearRes = Integer.parseInt(startDateArray[0]);
                    //周期到期日的月份份为委托开始日期的月份加上周期 -1
                    monthRes = Integer.parseInt(startDateArray[1]) + this.cycles -1;
                    //对应年月的 月份最后一天
                    dayRes = getActualMaximumNew(yearRes, monthRes);
                } else if((Integer.parseInt(startDateArray[1]) + this.cycles) == 13){

                    //==13 那么本计算周期的结束日应该是上一年年底的最后一天

                    //周期到期日的年份为当前年份
                    yearRes = Integer.parseInt(startDateArray[0]);
                    //周期到期日的月份份为委托开始日期的月份加上周期 -1
                    monthRes = 12;
                    //对应年月的 月份最后一天
                    dayRes = DateUtil.getEndDateTimeOfMonth(yearRes, monthRes);
                } else {
                    //周期到期日的年份为当前年份
                    yearRes = Integer.parseInt(startDateArray[0]) + 1;
                    //周期到期日的月份份为委托开始日期的月份加上周期 -1
                    monthRes = Integer.parseInt(startDateArray[1]) + this.cycles -12 -1;
                    //对应年月的 月份最后一天
                    dayRes = DateUtil.getEndDateTimeOfMonth(yearRes, monthRes);
                }
            }

            String res = yearRes + "-" + monthRes + "-" + dayRes;
            Date resDate = null;
            try {
                resDate = ft.parse(res);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.endDate = resDate;
        }
        private Integer getActualMaximumNew(int year,int month){
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMM");
            Date dd=null;
            try {
                dd = sdf0.parse(year+""+month);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(dd);
            return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        public Date getEndDate() {
            return endDate;
        }
    }

    public static String getDateString(Date date, String dateFormate) {
        if (date == null)
            return "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getCurDateTime();
    }

    public static String getCurDateTime() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(now.getTime());
    }

    public static Date getDateFormat(String date, String format) {
        SimpleDateFormat sdf;
        try {
            sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /////////////////copy By isz_api
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH)+1;
    }

    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取“yyyy/MM/dd HH:mm:ss”格式的时间
     */
    public static  String getAppDateFormat(String data){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
        try {
            Date dt = sdf.parse(data);
            SimpleDateFormat format = new SimpleDateFormat(APP_DATE_FORMAT_PATTERN);
            data= format.format(dt);
            return 	data;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }
    /**
     * 获取“yyyy/MM/dd HH:mm”格式的时间
     */
    public static  String getAppdateStrToyyyyMMddHHmm(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
        try {
            if(StringUtils.isNotBlank(date)){
                Date dt = sdf.parse(date);
                SimpleDateFormat format = new SimpleDateFormat(APP_MM_DATE_FORMAT_PATTERN);
                date= format.format(dt);
                return 	date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static  String getAppDateStr(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
        try {
            if(StringUtils.isNotBlank(date)){
                Date dt = sdf.parse(date);
                SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
                date= format.format(dt);
                return 	date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String changeToAppDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
        try {
            if(StringUtils.isNotBlank(date)){
                Date dateTime = parse(date,APP_MM_DATE_FORMAT_PATTERN);
                date= sdf.format(dateTime);
                return 	date;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     *
     * @Description:计算核发月份
     * @param achievementDate
     * @param submitDate
     * @return
     * String
     * @exception:
     * @author: zhangqy
     * @time:2017年5月20日 上午11:44:51
     */
   /* public static  String createAchievementMonth(Date achievementDate,Date submitDate){

        Assert.isAfterOrEquals(achievementDate, submitDate, "业绩达成时间要在提交时间之后");
        Calendar cal = Calendar.getInstance();
        cal.setTime(submitDate);
        cal.add(Calendar.MONTH, 1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), ACHIEVE_DAY,ACHIEVE_HOUR,ACHIEVE_MIN);
        Date beginDate = cal.getTime();
        while(beginDate.before(achievementDate)){
            cal.add(Calendar.MONTH, 1);
            beginDate = cal.getTime();

        }
        SimpleDateFormat  sdf = new SimpleDateFormat(ACHIEVE_MONTH_PARSE);
        cal.add(Calendar.MONTH, -1);
        return sdf.format(cal.getTime());

    }*/
    /**
     *
     * @Description:设置核发月份
     * @param submitDate
     * @return
     * String
     * @exception:
     * @author: zhangqy
     * @time:2017年5月20日 下午12:45:23
     */
    public static String getAchievmentMonth(Date submitDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(submitDate);
        SimpleDateFormat  sdf = new SimpleDateFormat(ACHIEVE_MONTH_PARSE);
        return sdf.format(cal.getTime());
    }

    /**
     * @param dateStr
     * @return
     */
    public static String dateStrToyyyyMMdd(String dateStr) {
        if (null != dateStr && dateStr.length() >= 10) {
            return dateStr.substring(0, 10);
        }
        return dateStr;
    }

    /**
     * 解析字符串成java.util.Date
     *
     * @param str
     * @return
     */
    public static Date parseStrToDate(String str) {
        String pattern="yyyyMMddHHmmss";
        if(str!=null){
            str=str.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "").replaceAll("\\.", "");
            int len=str.length();
            if(len==17){
                pattern="yyyyMMddHHmmssSSS";
            }else if(len==14){
                pattern="yyyyMMddHHmmss";
            }else if(len==12){
                pattern="yyyyMMddHHmm";
            }else if(len==10){
                pattern="yyyyMMddHH";
            }else if(len==8){
                pattern="yyyyMMdd";
            }else if(len==6){
                pattern="yyyyMM";
            }else if(len==4){
                pattern="yyyy";
            }
        }
        return parse(str, pattern);
    }

    /**
     * 把1970改为空值
     * @param dateTime
     * @return
     */
    public static String returnDefaultTime(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            if (dateTime.startsWith("1970-01-0")) {
                return null;
            }
        }
        return dateTime;
    }

    /**
     * @Description: 两个日期之间间隔分钟
     * @return int
     * @author luoxiaofei
     * @date 2017年8月30日 下午4:15:11
     */
    public static final int minuteBetween(String early, String late) {
        Date dateEarly = parse(early);
        Date dateLate = parse(late);
        return minuteBetween(dateEarly, dateLate);
    }
    /**
     * 计算两个日期之间的天数
     */
    public static final int minuteBetween(Date early, Date late) {
        Calendar ecal = Calendar.getInstance();
        Calendar lcal = Calendar.getInstance();
        ecal.setTime(early);
        lcal.setTime(late);

        long etime = ecal.getTimeInMillis();
        long ltime = lcal.getTimeInMillis();
        return (int) ((ltime - etime) / MILLIS_PER_MINUTE);
    }


    /**
     *
     * @Description:计算正常出房业绩核发月份
     * @param achievementTime  合同生效日期
     * @param financeTime  首期款达成日期
     * @param submitTime  合同提交日期
     * @return String
     * @author: jinlin
     * @exception:
     * @time:2017年8月03日 上午11:44:51
     */
    public static  String createNormalAchievementMonth(String achievementTime,String financeTime, String submitTime){
        Calendar cal = Calendar.getInstance();
        Date submitDate = DateUtil.parse(submitTime);
        Date financeDate = DateUtil.parse(financeTime);
        Date achievementDate = DateUtil.parse(achievementTime);
        Date time1 = null; //生效时间计算所得
        Date time2 = null; //首期款时间计算所得
        SimpleDateFormat  sdf = new SimpleDateFormat(ACHIEVE_MONTH_PARSE);
        if(StringUtils.isNotBlank(financeTime)){
            cal.setTime(submitDate);
            cal.add(Calendar.MONTH, 4);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 24, 0);
            Date fsubmitDate4 = cal.getTime();  //合同提交月的次次次次月
            cal.setTime(fsubmitDate4);
            cal.add(Calendar.MONTH, -1);
            Date fsubmitDate3 = cal.getTime();
            if(!financeDate.after(fsubmitDate4)){
                time2 = fsubmitDate3;  //返回合同提交月次次次月的年月
            }
            cal.setTime(fsubmitDate3);
            cal.add(Calendar.MONTH, -1);
            Date fsubmitDate2 = cal.getTime();  //合同提交月次次月
            if(!financeDate.after(fsubmitDate3)){
                time2 = fsubmitDate2;  //返回合同提交月次次月的年月
            }
            cal.setTime(fsubmitDate2);
            cal.add(Calendar.MONTH, -1);
            Date fsubmitDate1 = cal.getTime();  //合同提交月次次次月
            if(!financeDate.after(fsubmitDate2)){
                time2 = fsubmitDate1;  //返回合同提交月次月的年月
            }
            if(!financeDate.after(fsubmitDate1)){
                time2 = submitDate;  //返回合同提交月的年月
            }

            cal.setTime(submitDate);
            cal.add(Calendar.MONTH, 3);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), ACHIEVE_DAY, ACHIEVE_HOUR, ACHIEVE_MIN);
            Date asubmitDate3 = cal.getTime();
            cal.setTime(asubmitDate3);
            cal.add(Calendar.MONTH, -1);
            Date asubmitDate2 = cal.getTime();
            if(achievementDate.after(asubmitDate2) && !achievementDate.after(asubmitDate3)){
                time1 = asubmitDate2;
            }
            cal.setTime(asubmitDate2);
            cal.add(Calendar.MONTH, -1);
            Date asubmitDate1 = cal.getTime();
            if(achievementDate.after(asubmitDate1) && !achievementDate.after(asubmitDate2)){
                time1 = asubmitDate1;
            }
            if(!achievementDate.after(asubmitDate1)){
                time1 = submitDate;
            }
            if(time1 != null && time2 != null){
                if(time1.before(time2)){
                    return sdf.format(time2);
                }else{
                    return sdf.format(time1);
                }
            }else if(time1 != null || time2 != null){
                return sdf.format(time1 == null ? time2:time1);
            }

        }
        return null;
    }

    /**
     * 获取周岁
     * @param birthday 生日
     * @return int
     */
    public static int getCurrentAge(Date birthday) {
        Calendar curr = Calendar.getInstance();
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);
        int age = curr.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        int currentMonth = curr.get(Calendar.MONTH);
        int currentDay = curr.get(Calendar.DAY_OF_MONTH);
        int bronMonth = born.get(Calendar.MONTH);
        int bronDay = born.get(Calendar.DAY_OF_MONTH);
        if (currentMonth < bronMonth || (currentMonth == bronMonth && currentDay < bronDay)) {
            age--;
        }
        return age < 0 ? 0 : age;
    }

    /**
     * 标准时间转为北京时间
     * @param date
     * @return
     */
    public static Date formatUTCToCST(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 8);
        return calendar.getTime();
    }

    /**
     * 北京时间转为标准时间
     * @param date
     * @return
     */
    public static Date formatCSTToUTC(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -8);
        return calendar.getTime();
    }

}


