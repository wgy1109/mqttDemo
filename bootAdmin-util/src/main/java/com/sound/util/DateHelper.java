package com.sound.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	
	/**
	 * 把时间转换成相应格式的字符串  , 默认返回当前时间'yyyy-MM-dd HH:mm:ss' 格式 
	 * @param date	传入时间
	 * @param type	要得到的时间类型
	 * @return
	 */
	public static String getDateString(Date date, String type){
		try {
			if(date == null ){
				date = new Date();
			}
			SimpleDateFormat sdf = new SimpleDateFormat(type);
			String result = sdf.format(date);
			return result ;
		} catch (Exception e) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String result = sdf.format(new Date());
			return result ;
		}
	}
	
	public static String getOtherDateString(Integer d, String type){
		Calendar   cal   =   Calendar.getInstance();
	    cal.add(Calendar.DATE,   d);
	    String day = new SimpleDateFormat(type).format(cal.getTime());
		return day;
	}
	
	public static String getOtherMonthString(Integer d, String type){
		Calendar   cal   =   Calendar.getInstance();
	    cal.add(Calendar.MONTH,   d);
	    String day = new SimpleDateFormat(type).format(cal.getTime());
		return day;
	}
	
	public static String getOtherHourString(Integer d, String type){
		Calendar   cal   =   Calendar.getInstance();
	    cal.add(Calendar.HOUR,   d);
	    String day = new SimpleDateFormat(type).format(cal.getTime());
		return day;
	}
	
	/**
	 * 把Timestamp时间转换成相应格式的字符串  , 默认返回当前时间'yyyy-MM-dd HH:mm:ss' 格式 
	 * @param date	传入时间
	 * @param type	要得到的时间类型
	 * @return
	 */
	public static String getTimestampString(Timestamp date, String type){
		try {
			if(date == null ){
				date = new Timestamp(System.currentTimeMillis());
			}
			DateFormat sdf = new SimpleDateFormat(type);
			String result = sdf.format(date);
			return result ;
		} catch (Exception e) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String result = sdf.format(new Date());
			return result ;
		}
	}
	
	//固定格式，返回 yyMMdd....字符串
	public static String getDateStringTimg(String formdata){
		if(Tools.notEmpty(formdata)){
			String result = formdata.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "").substring(2) + "000000000000";
			return result;
		}else{
			return "";
		}
	}
	
	//固定格式，返回 yyMMdd....字符串
	public static String getDateStringTimgToday(String formdata){
		if(Tools.notEmpty(formdata)){
			String result = formdata.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "").substring(2) + "000000000000";
			return result;
		}else{
			DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String result = sdf.format(new Timestamp(System.currentTimeMillis()));
			return result.substring(2) + "000000000000";
		}
	}
	
	public static String getNextDayString(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(date);
			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date1);
		    cal.add(Calendar.DATE,  1);
		    String day = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			return day+"-00";
		} catch (ParseException e) {
			return getOtherDateString(1, "yyyy-MM-dd")+"-00";
		}
	}
	public static String getNextDatetimeString(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = sdf.parse(date);
			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date1);
		    cal.add(Calendar.DATE,  1);
		    String day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			return day;
		} catch (ParseException e) {
			return getOtherDateString(1, "yyyy-MM-dd HH:mm:ss");
		}
	}
	public static String getOtherDayString(String date, Integer num){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(date);
			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date1);
		    cal.add(Calendar.DATE,  num);
			return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		} catch (ParseException e) {
			return getOtherDateString(num, "yyyy-MM-dd");
		}
	}
	
	public static String getOtherMonthString(String date, Integer num){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(date);
			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date1);
		    cal.add(Calendar.MONTH,  num);
			return new SimpleDateFormat("yyyy-MM").format(cal.getTime());
		} catch (ParseException e) {
			return getOtherDateString(0, "yyyy-MM");
		}
	}
	
	public static String getCurrentStage(){
		Date d = new Date();
		int hours = d.getHours()-1;
		return hours/6+1+"";
	}
	
	public static String getBeforeDayString(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(date);
			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date1);
		    cal.add(Calendar.DATE,  -1);
		    String day = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			return day;
		} catch (ParseException e) {
			return getOtherDateString(1, "yyyy-MM-dd");
		}
	}
	
	public static int getWeekBydate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cl = Calendar.getInstance(); 
		try {
			cl.setTime(sdf.parse(date)); 
			return  cl.get(Calendar.WEEK_OF_YEAR); 
		} catch (ParseException e) {
			cl.setTime(new Date()); 
			return  cl.get(Calendar.WEEK_OF_YEAR); 
		} 
	}
	
	public static int daysOfTwo(String fDate, String oDate) {
	   try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   Calendar aCalendar = Calendar.getInstance();
		   aCalendar.setTime(sdf.parse(fDate));
		   int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		   aCalendar.setTime(sdf.parse(oDate));
		   int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		   return day2 - day1;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
    }
	
	public static int weeksOfTwo(String fDate, String oDate) {
	   try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   Calendar aCalendar = Calendar.getInstance();
		   aCalendar.setTime(sdf.parse(fDate));
		   int day1 = aCalendar.get(Calendar.WEEK_OF_YEAR);
		   aCalendar.setTime(sdf.parse(oDate));
		   int day2 = aCalendar.get(Calendar.WEEK_OF_YEAR);
		   return day2 - day1;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
    }
	
	public static int monthsOfTwo(String fDate, String oDate) {
		   try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			   Calendar aCalendar = Calendar.getInstance();
			   aCalendar.setTime(sdf.parse(fDate));
			   int day1 = aCalendar.get(Calendar.MONTH);
			   aCalendar.setTime(sdf.parse(oDate));
			   int day2 = aCalendar.get(Calendar.MONTH);
			   return day2 - day1;
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
	    }
	
	//获取指定月份的天数  
    public static int getDaysByYearMonth(int year, int month) {  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }  
	
}
