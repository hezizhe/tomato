package com.lv.qq.client.util;

import java.io.File;
import java.math.BigDecimal;

public class FileSizeFormatTool {

	public static String formatFileSize(long length){
		if (length > 1024 * 1024 * 1024) {
			double f = Double.valueOf(length) / (1024 * 1024 * 1024);
			return  new BigDecimal(f).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "GB";
		} else if (length > 1024 * 1024) {
			double f = Double.valueOf(length) / (1024 * 1024);
			return  new BigDecimal(f).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "MB";
		} else if (length > 1024) {
			double f = Double.valueOf(length) / 1024;
			return  new BigDecimal(f).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue() + "KB";
		} else {
			return length + "×Ö½Ú";
		}
	}
	
	public static String formatFileSize(File file){
		return formatFileSize(file.length());
	}
	
}
