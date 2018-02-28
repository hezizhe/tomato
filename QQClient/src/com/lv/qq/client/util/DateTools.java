package com.lv.qq.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {
	
	public static String formatTime(Date date, String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date getDateByString(String source, String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int countAge(Date birthday){
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		Calendar birth = Calendar.getInstance();
		birth.setTime(birthday);
		if (birth.after(now)) {
			return 0;
		} else {
			int year = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
			if(birth.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)){ //已经过了生日
				return year;
			}else {
				return year == 0 ? 0 : year - 1;
			}
		}
	}
	
	public static Date addYear(Date date, int year){
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.YEAR, year);
		return calender.getTime();
	}

}
