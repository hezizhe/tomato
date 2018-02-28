package com.lv.qq.client.config;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.common.vo.Dictionary;

public class PersonalConfig {
	
	private static Map<String, Integer> styleMap = new HashMap<String, Integer>();
	public static String[] FONT_HOME;
	public static String DOWNLOAD_PATH;
	public static String[] CITYS = {"���� �人", "���� ��ʯ", "���� ʮ��", "���� �˲�", "���� ����", "���� ����", 
									"���� ����", "���� Т��", "���� ����", "���� �Ƹ�", "���� ����", "���� ����",
									"���� ��ʩ", "���� ����", "���� Ǳ��", "���� ����", "���� ������"};
	public static List<Dictionary> GENDER_LIST = new ArrayList<Dictionary>();
	public static List<Dictionary> PROFESSION_LIST = new ArrayList<Dictionary>();
	public static List<Dictionary> BLOOD_LIST = new ArrayList<Dictionary>();
	public static Map<String, Integer> GENDER_NAME_MAP = new HashMap<String, Integer>();
	public static Map<Integer, String> GENDER_VALUE_MAP = new HashMap<Integer, String>();
	public static Map<String, Integer> PROFESSION_NAME_MAP = new HashMap<String, Integer>();
	public static Map<Integer, String> PROFESSION_VALUE_MAP = new HashMap<Integer, String>();
	public static Map<String, Integer> BLOOD_NAME_MAP = new HashMap<String, Integer>();
	public static Map<Integer, String> BLOOD_VALUE_MAP = new HashMap<Integer, String>();
	public static String[] AGES = {"18������", "18-22��", "23-26��", "27-35��", "35������"};
	
	public static String EXPRESSION_PATH = "C:/Users/Administrator/Desktop/expression";
	
	static{
		styleMap.put("��ͨ", Font.PLAIN);
		styleMap.put("����", Font.BOLD);
		styleMap.put("б��", Font.ITALIC);
		
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();  
		FONT_HOME = e.getAvailableFontFamilyNames();  
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File desktop = fsv.getHomeDirectory();
		DOWNLOAD_PATH = desktop.getPath();
	}
	
	public static int FONT_SIZE = 14;
	public static String FONT_NAME = "����";
	public static int FONT_STYLE = Font.PLAIN;
	
	public static void setFontStyle(String style){
		if(styleMap.get(style) != null)
			FONT_STYLE = styleMap.get(style);
	}
	
	public static String getFontStyle(){
		for (Entry<String, Integer> entry : styleMap.entrySet()) {
			if(entry.getValue() == FONT_STYLE)
				return entry.getKey();
		}
		return null;
	}

	public static Set<String> getStyleSet() {
		return styleMap.keySet();
	}
	
	public static void init(ClientControl control){
		Map<String, List<Dictionary>> dictionarys = control.getDictionarys();
		GENDER_LIST = dictionarys.get("gender");
		PROFESSION_LIST = dictionarys.get("profession");
		BLOOD_LIST = dictionarys.get("blood_type");
		for (Dictionary gender : GENDER_LIST) {
			GENDER_NAME_MAP.put(gender.getName(), gender.getValue());
			GENDER_VALUE_MAP.put(gender.getValue(), gender.getName());
		}
		for (Dictionary profession : PROFESSION_LIST) {
			PROFESSION_NAME_MAP.put(profession.getName(), profession.getValue());
			PROFESSION_VALUE_MAP.put(profession.getValue(), profession.getName());
		}
		for (Dictionary blood : BLOOD_LIST) {
			BLOOD_NAME_MAP.put(blood.getName(), blood.getValue());
			BLOOD_VALUE_MAP.put(blood.getValue(), blood.getName());
		}
	}
}
