package com.lv.qq.util;

import java.util.regex.Pattern;

public class StringTools {

	/**
	 * �ж��ַ����Ƿ�Ϊ������
	 * @return
	 */
	public static boolean isPosiInt(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches(); 
	}

}
