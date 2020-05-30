package com.deepthink.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author zhangxiaoman
 * @date 2019年8月12日
 * @desc 关于字符串操作的工具方法
 */
public class StringUtils {
	
	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);
	
	public boolean isStringEmpty(String str) {
		if(str == null||"".equals(str)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 获取异常的堆栈信息
	 * 
	 * @param e
	 * @return string
	 */
	public static String toString(Throwable e) {
		UnsafeStringWriter w = new UnsafeStringWriter();
		PrintWriter p = new PrintWriter(w);
		p.print(e.getClass().getName());
		if (e.getMessage() != null) {
			p.print(": " + e.getMessage());
		}
		p.println();
		try {
			e.printStackTrace(p);
			return w.toString();
		} finally {
			p.close();
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 将空字符串变为null
	 * 
	 * @param str
	 * @return
	 */
	public static String isNull(String str) {
		if (null == str || "".equals(str)) {
			str = null;
		} else {
			str = str.trim();
		}
		return str;
	}

	/**
	 * 将字符串变为模糊处理
	 * 
	 * @param str
	 * @return
	 */
	public static String fuzzyProcessing(String str) {
		if (null == str || "".equals(str)) {
			str = null;
		} else {
			str = "%" + str.trim() + "%";
		}
		return str;
	}
	
	/**
	 * 手机号脱敏
	 */
	public static String phoneNumMask(String phoneNum) {
		String maskedPhoneNum = phoneNum;
		if (StringUtils.isNotEmpty(phoneNum) && phoneNum.length() > 7) {
			maskedPhoneNum = new StringBuffer(phoneNum).replace(3,7,"****").toString();
		}
		return maskedPhoneNum;
	}

	/**
	 * 字符串格式日期转为日期格式
	 * 
	 * @param date1
	 * @return
	 */
	public static Date stringToDate1(Date date1) {
		logger.info("stringToDate1 invoke.date1:" + date1);
		Calendar calendar = Calendar.getInstance();
		if (date1 != null) {
			calendar.setTime(date1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			date1 = calendar.getTime();
		}
		return date1;
	}

	/**
	 * 字符串格式日期转为日期格式
	 * 
	 * @param date2
	 * @return
	 */
	public static Date stringToDate2(Date date2) {
		logger.info("stringToDate2 invoke.date2:" + date2);
		Calendar calendar = Calendar.getInstance();
		if (date2 != null) {
			calendar.setTime(date2);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			date2 = calendar.getTime();
		}
		return date2;
	}

	/**
	 * 手机号隐藏中间4位
	 * 
	 * @param phoneNum
	 * @return
	 */
	public static String hidePhoneNum(String phoneNum) {
		if (phoneNum == null || "".equals(phoneNum)) {
			return null;
		} else {
			String hidePhoneNum = phoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
			return hidePhoneNum;
		}
	}

	/**
	 * 银行卡显示后4位
	 * 
	 * @param cardNum
	 * @return
	 */
	public static String hideCardNum(String cardNum) {
		if (cardNum == null || "".equals(cardNum)) {
			return null;
		} else if (cardNum.length() <= 4) {
			logger.info("该银行卡号长度小于5.cardNum:" + cardNum);
			return null;
		} else {
			int cardNumLenth = cardNum.length();
			int afterLength = 4;
			String replaceSymbol = "*";
			StringBuffer hideCardNum = new StringBuffer();
			for (int i = 0; i < cardNumLenth; i++) {
				if (i >= (cardNumLenth - afterLength)) {
					hideCardNum.append(cardNum.charAt(i));
				} else {
					hideCardNum.append(replaceSymbol);
				}
			}
			return hideCardNum.toString();
		}
	}

	/**
	 * 判断列表1是否被列表2包含
	 * 
	 * @param      <E>
	 * @param list
	 * @return
	 */
	public static <E> boolean judgeListOneIncluTwo(List<E> list_1, List<E> list_2) {
		for (int i = 0; i < list_1.size(); i++) {
			if (!list_2.contains(list_1.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * sortType参数处理
	 * 
	 * @param date
	 * @return
	 */
	public static String sortType(String tableName, String sortType) {
		if (sortType == null) {
			sortType = tableName + "." + "id";
			logger.info("sortType is null.sortType:" + sortType);
		} else {
			sortType = tableName + "." + sortType;
		}
		return sortType;
	}

	/**
	 * sortDir参数处理
	 * 
	 * @param date
	 * @return
	 */
	public static String sortDir(Integer sortDirInt) {
		String sortDir = "DESC";
		if (sortDirInt == null || sortDirInt == -1) {
			logger.info("sortDir is null.sortDir:" + sortDirInt);
		} else {
			sortDir = "";
		}
		return sortDir;
	}

	/**
	 * 检查手机号
	 * 
	 * @param phoneNum
	 * @return
	 */
	public static boolean checkPhoneNum(String phoneNum) {
		logger.info("checkPhoneNum invoke.phoneNum:" + phoneNum);
		if (phoneNum == null) {
			logger.info("phoneNum is null.phoneNum:" + phoneNum);
			return false;
		} else {
			String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phoneNum);
			boolean isMatch = m.matches();
			if (!isMatch) {
				logger.info("phoneNum error.phoneNum:" + phoneNum);
				return false;
			} else {
				logger.info("check ok.");
				return true;
			}
		}
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static JSONObject isNumeric(JSONObject checkNumObject) {
		logger.info("isNumeric invoke.checkNumObject:" + checkNumObject.toJSONString());
		JSONObject ret = new JSONObject();
		if (checkNumObject.size() == 0) {
			logger.info("checkNumObject is null.checkNumObject:" + checkNumObject);
			ret.put("check", false);
		} else {
			for (String str : checkNumObject.keySet()) {
				String value = checkNumObject.getString(str);
				if (value == null) {
					logger.info("value is null.value:" + value);
					ret.put("check", true);
					ret.put(str, null);
				} else {
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher isNum = pattern.matcher(value);
					if (!isNum.matches()) {
						logger.info("value is not numeric.value:" + value);
						ret.put("check", false);
						break;
					}
					ret.put("check", true);
					ret.put(str, value);
				}
			}
		}
		return ret;
	}

	/**
	 * 检查提现日期
	 * 
	 * @param date1
	 * @param date2 不能超过今天
	 * @return
	 */
	public static boolean checkWithdrawDate(Date date1, Date date2) {
		logger.info("checkWithdrawDate invoke.date1:" + date1 + ",date2:" + date2);
		if (date1.getTime() >= date2.getTime()) {
			logger.error("date1 bigger than date2.date1:" + date1 + ",date2:" + date2);
			return false;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			if (date2.getTime() >= calendar.getTime().getTime()) {
				logger.error("date2 bigger than today.date2:" + date2);
				return false;
			} else {
				logger.info("check ok");
				return true;
			}
		}
	}

	/**
	 * 计算银行提现费用
	 * 
	 * @param actualAmount
	 * @return
	 */
	public static Float calBankFee(Float actualAmount) {
		logger.info("calBankFee invoke.actualAmount:" + actualAmount);
		Float bankFee = (float) 0;
		bankFee = (float) (Math.round(actualAmount / 1001 * 100) / 100.00);
		if (0 < bankFee && bankFee <= 1) {
			bankFee = (float) 1;
		} else if (bankFee >= 25) {
			bankFee = (float) 25;
		}
		logger.info("bankFee:" + bankFee);
		return bankFee;
	}

	/**
	 * 判断列表A是否是列表B的子集
	 * 
	 * @param listA
	 * @param listB
	 * @return
	 */
	public static boolean isSubsetList(List<Long> listA, List<Long> listB) {
		logger.info("isSubsetList invoke.listA:" + Arrays.toString(listA.toArray()) + ",listB:"
				+ Arrays.toString(listB.toArray()));
		if (listA == null || listB == null) {
			logger.info("listA or listB is null.");
			return false;
		}
		for (Object object : listA) {
			if (!listB.contains(object)) {
				logger.info(object + " not in listB.");
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串格式日期转为日期格式
	 * 
	 * @param date1
	 * @return
	 */
	public static Date stringToDate1Dashboard(Date date1) {
		logger.info("stringToDate1Dashboard invoke.date1:" + date1);
		Calendar calendar = Calendar.getInstance();
		if (date1 == null) {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			date1 = calendar.getTime();
		} else {
			calendar.setTime(date1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			date1 = calendar.getTime();
		}
		return date1;
	}

	/**
	 * 字符串格式日期转为日期格式
	 * 
	 * @param date2
	 * @return
	 */
	public static Date stringToDate2Dashboard(Date date2) {
		logger.info("stringToDate1Dashboard invoke.date2:" + date2);
		Calendar calendar = Calendar.getInstance();
		if (date2 == null) {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			date2 = calendar.getTime();
		} else {
			calendar.setTime(date2);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			date2 = calendar.getTime();
		}
		return date2;
	}

	/**
	 * 列表去重复
	 * 
	 * @param 列表
	 * @return 列表
	 */
	public static <T> List<T> removeListDup(List<T> list) {
		logger.info("removeListDup invoke.list:" + Arrays.toString(list.toArray()));
		List<T> newList = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			if (!newList.contains(list.get(i))) {
				newList.add(list.get(i));
			}
		}
		return newList;
	}

	/**
	 * 检查参数是否为空
	 * 
	 * @param params
	 */
	public static boolean checkParamsIsNull(JSONObject params) {
		logger.info("checkParamsIsNull invoke.params:" + params);
		if (params == null || params.size() == 0) {
			logger.error("params is null.params:" + params);
			return false;
		} else {
			for (String str : params.keySet()) {
				if (params.get(str) instanceof JSONArray || params.get(str) instanceof JSONObject) {
					if (params.get(str) == null || params.size() == 0) {
						logger.info(str + " is null.str:" + str);
						return false;
					}
				}
				if (null == params.get(str) || "".equals(params.get(str))) {
					logger.info(str + " is null.str:" + params.get(str));
					return false;
				}
			}
			return true;
		}
	}

//	public static void main(String[] args) {
//		JSONObject params = new JSONObject();
//		params.put("a", "aa");
//		params.put("b", 1.3);
//		JSONObject jsonObject = null;
////		jsonObject.put("1", 1);
//		params.put("c", jsonObject);
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.add(1);
//		params.put("d", jsonArray);
//		System.out.println(checkParamsIsNull(params));
//	}
}
