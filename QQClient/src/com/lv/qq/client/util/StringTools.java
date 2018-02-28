package com.lv.qq.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {

	/**
	 * 判断字符串是否为正整数
	 * @return
	 */
	public static boolean isPosiInt(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches(); 
	}
	
	public static List<String> getMatch(String str, String patten){
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(patten);
		Matcher m = p.matcher(str);
		while (m.find()) {
			list.add(m.group(1));
		}
		return list;
	}
	
}
