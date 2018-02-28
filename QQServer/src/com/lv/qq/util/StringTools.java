package com.lv.qq.util;

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

}
