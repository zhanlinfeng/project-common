package com.deepthink.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

	/**
	 * 将Date类型转换为字符串类型日期
	 * @param date
	 * @return
	 * @throws ParseException
	 * String
	 * TODO
	 */
	public static  String formatDate(Date date){
       SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return sdf.format(date);
   }
   
	/**
	 * 将字符串类型日期转换为Date类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 * Date
	 * TODO
	 */
   public static Date parse(String strDate) throws ParseException{
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return sdf.parse(strDate);
   }
   
   /**
    * 将某格式的时间字符串转化成毫秒时间戳表示的字符串
    * @param dateTime
    * @param format
    * @return
    * String
    * TODO
    */
   public static String dateTimeStrToMills(String dateTime,String format){
       String dateStr = null;
       SimpleDateFormat sdf = new SimpleDateFormat(format);
       Calendar calendar = Calendar.getInstance();
       calendar.clear();
       try {
           Date d = new Date();
           d = sdf.parse(dateTime);
           calendar.setTime(d);
           dateStr = calendar.getTimeInMillis()+"ms";
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return dateStr;
   }
   
   /**
    * 获取某个月的天数
    * @param year
    * @param month
    * @return
    * int
    * TODO
    */
   public static int getDayNumOfMonth(int year,int month){
       Calendar calendar = Calendar.getInstance();
       calendar.clear();
       calendar.set(year, month,0);
       return calendar.get(Calendar.DAY_OF_MONTH);
   }
   
   /**
    * 获取某日、月、年前后的日期
    * @param num
    * @param date
    * @param format
    * @param timeType
    * @return
    * String
    * TODO
    */
   public static String getBeforeOrAfterDateType(int num,String date,String format,int timeType){
       SimpleDateFormat sdf = new SimpleDateFormat(format);
       String resultDate = "";
       Calendar calendar = Calendar.getInstance();
       calendar.clear();
       try {
           Date d = new Date();
           d = sdf.parse(date);
           calendar.setTime(d);
           calendar.add(timeType, num);//一天的结束是第二天的开始
           resultDate = sdf.format(calendar.getTime());
       } catch (ParseException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       return resultDate;
   }
   
   /**
    * 根据毫秒时间戳获得格式化后的日期
    * @param millisecond
    * @param dateFormat
    * @return
    * String
    * TODO
    */
   public static String millisecondToDate(Long millisecond,String dateFormat){
       Date date = new Date(millisecond);
       GregorianCalendar gc = new GregorianCalendar();
       gc.setTime(date);
       SimpleDateFormat format = new SimpleDateFormat(dateFormat);
       String sb  = format.format(gc.getTime());
       return sb;
   }
   
   /**
    * 获取某月第一天
    * @param year
    * @param month
    * @return
    * String
    * TODO
    */
   public static String getFirstDayOfMonth(int year,int month){
       Calendar cal = Calendar.getInstance();
       cal.set(Calendar.YEAR, year);
       cal.set(Calendar.MONTH, month-1);
       int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
       cal.set(Calendar.DAY_OF_MONTH, firstDay);
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       String firstDayOfMonth = sdf.format(cal.getTime());
       return firstDayOfMonth;
   }
   
   /**
    * 获取某月最后一天：
    * @param year
    * @param month
    * @param format
    * @return
    * String
    * TODO
    */
   public static String getLastDayOfMonth(int year,int month,String format){
       Calendar cal = Calendar.getInstance();
       cal.clear();
       cal.set(Calendar.YEAR, year);
       cal.set(Calendar.MONTH, month-1);
       int lastDay = 0;
       lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
       cal.set(Calendar.DAY_OF_MONTH, lastDay);
       SimpleDateFormat sdf = new SimpleDateFormat(format);
       String lastDayOfMonth = sdf.format(cal.getTime());
       return lastDayOfMonth;
   }
   
   /**
    * 获取起止日期之间的所有字符串（可自定义间隔、格式、日期类型）
    * @param begin
    * @param end
    * @param num
    * @param timeType
    * @param format
    * @return
    * List<String>
    * TODO
    */
   public static List<String> getDatesBetweenTwoDate(String begin,String end,int num,int timeType,String format){
       List<String> lDates;
       lDates = new ArrayList<String>();
       try {
           SimpleDateFormat sdf = new SimpleDateFormat(format);
           Date beginDate = sdf.parse(begin);
           Date endDate = sdf.parse(end);
           lDates.add(sdf.format(beginDate));
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(beginDate);
           boolean bContinue = true;
           while(bContinue){
               calendar.add(timeType, num);
               if(endDate.after(calendar.getTime())){
                   lDates.add(sdf.format(calendar.getTime()));
               }else{
                   break;
               }
           }
           lDates.add(sdf.format(endDate));
       } catch (ParseException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       return lDates;
   }
}
