package com.my.test.demo.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 验证工具类
 */
@Slf4j
public class ValidateUtil {
	private ValidateUtil() {
		throw new IllegalStateException("Utility class");
	}

	
	/** 
	 * @Title: isMobileNO
	 * @Description:验证手机号是否合法
	 * @param phone
	 * @return  
	 */  
	public static boolean isMobileNO(String phone){
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (phone.length() != 11) {
			log.info("手机号应为11位数");
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			if (!isMatch) {
				log.error("手机号不合法");
			}
			return isMatch;
		}
	}

	/** 
	 * @Title: isEmail
	 * @Description: 验证邮箱号是否合法
	 * @param email
	 * @return  
	 */  
	public static boolean isEmail(String email){
		if (StringUtils.isBlank(email)) {
			return false;
		}
		Pattern emailP = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher emailM = emailP.matcher(email);
		if (!emailM.matches()) {
			return false;
		}
		return true;
	}


	/**
	 * 大陆地区地域编码最大值
	 **/
	private static final int MAX_MAINLAND_AREACODE = 659004;
	/**
	 * 大陆地区地域编码最小值
	 **/
	private static final int MIN_MAINLAND_AREACODE = 110000;
	/**
	 * 香港地域编码值
	 **/
	private static final int HONGKONG_AREACODE = 810000;                                                                                                                                                 // 香港地域编码值
	/**
	 * 台湾地域编码值
	 **/
	private static final int TAIWAN_AREACODE = 710000;
	/**
	 * 澳门地域编码值
	 **/
	private static final int MACAO_AREACODE = 820000;

	/**
	 * 数字正则
	 **/
	private static final String REGEX_NUM = "^[0-9]*$";
	/**
	 * 闰年生日正则
	 **/
	private static final String REGEX_BIRTHDAY_IN_LEAP_YEAR = "^((19[0-9]{2})|(20[0-9]{2}))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))$";
	/**
	 * 平年生日正则
	 **/
	private static final String REGEX_BIRTHDAY_IN_COMMON_YEAR = "^((19[0-9]{2})|(20[0-9]{2}))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))$";


	/** 
	 * @Title: isIdentityNum
	 * @Description:验证身份证是否合法
	 * @param identityNum
	 * @return  
	 */  
	public static boolean isIdentityNum(String identityNum){
		/**
		 * <p>
		 * 身份证格式强校验
		 * </p>
		 * <p>
		 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
		 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
		 * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S =
		 * Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5
		 * 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
		 * </p>
		 */

		if (StringUtils.isBlank(identityNum)) {
			return false;
		}
		String trimIdentityNum = identityNum.trim();

		if (!checkIdNumberRegex(trimIdentityNum)) {
			return false;
		}
		if (!checkIdNumberArea(trimIdentityNum.substring(0, 6))) {
			return false;
		}
		String newTrimIdentityNum = convertFifteenToEighteen(trimIdentityNum);
		if (!checkBirthday(newTrimIdentityNum.substring(6, 14))) {
			return false;
		}
		if (!checkIdNumberVerifyCode(newTrimIdentityNum)) {
			return false;
		}
		return true;
	}

	/**
	 * 身份证正则校验
	 */
	public static boolean checkIdNumberRegex(String idNumber) {
		return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
	}

	/**
	 * 身份证地区码检查
	 */
	private static boolean checkIdNumberArea(String idNumberArea) {
		int areaCode = Integer.parseInt(idNumberArea);
		if (areaCode == HONGKONG_AREACODE || areaCode == MACAO_AREACODE || areaCode == TAIWAN_AREACODE) {
			return true;
		} else {
			return (areaCode <= MAX_MAINLAND_AREACODE && areaCode >= MIN_MAINLAND_AREACODE);
		}
	}

	/**
	 * 将15位身份证转换为18位
	 */
	private static String convertFifteenToEighteen(String idNumber) {
		if (15 != idNumber.length()) {
			return idNumber;
		}

		String newIdNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15);
		newIdNumber = newIdNumber + getVerifyCode(newIdNumber);
		return newIdNumber;
	}

	/**
	 * 根据身份证前17位计算身份证校验码
	 */
	private static String getVerifyCode(String idNumber) {
		if (!Pattern.matches(REGEX_NUM, idNumber.substring(0, 17))) {
			return "";
		}
		String[] valCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };

		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * Integer.parseInt(wi[i]);
		}
		return valCodeArr[sum % 11];
	}

	/**
	 * 身份证出生日期嘛检查
	 */
	private static boolean checkBirthday(String idNumberBirthdayStr) {
		Integer year = null;
		year = Integer.valueOf(idNumberBirthdayStr.substring(0, 4));
		if (null == year) {
			return false;
		}

		Date birthday = DateUtil.parse(idNumberBirthdayStr, "yyyyMMdd");
		Date now = new Date();
		if(birthday != null && birthday.getTime() > now.getTime()){
			return false;
		}

		if (isLeapYear(year)) {
			return Pattern.matches(REGEX_BIRTHDAY_IN_LEAP_YEAR, idNumberBirthdayStr);
		} else {
			return Pattern.matches(REGEX_BIRTHDAY_IN_COMMON_YEAR, idNumberBirthdayStr);
		}
	}

	/**
	 * 判断是否为闰年
	 */
	private static boolean isLeapYear(int year) {
		return (year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
	}

	/**
	 * 身份证校验码检查
	 */
	private static boolean checkIdNumberVerifyCode(String idNumber) {
		return getVerifyCode(idNumber).equalsIgnoreCase(idNumber.substring(17));
	}

	public static void main(String[] args) {
		boolean check = ValidateUtil.isIdentityNum("330109201902204812");
		System.out.println(check);
	}
}
