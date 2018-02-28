package com.lv.qq.client.test;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

import com.lv.qq.client.util.DateTools;

public class Test {

	public static void main(String[] args) {
		FontMetrics fm = new JLabel().getFontMetrics(new Font("defalt", 0, 15));
		String str = "�÷žͷŽ����Ѻ������ڸ��ο����ο����Һ��о�������͸߿Ƽ��Һŷѻ�������" +
				"�����͵¹�����<body>fhdhgjhgfdhgeg</body>���ӷ��漰�����ܴ�";
		String[] ss = str.split("(<body>.*?</body>)");
		int lineW = 0;
		String f = "";
		for (String s : ss) {
			for (int i = 0; i < s.length(); i++) {
				int w = fm.stringWidth(String.valueOf(s.charAt(i)));
				lineW += w;
				if(lineW > 400){
					String left = s.substring(0, i);
					String right = s.substring(i);
					s = left + "<br>" + right;
					lineW = 0;
					i += 3;
				}
				if(i == s.length() - 1){
					if(lineW > 370){
						s += "<br>";
						lineW = 30;
					}else {
						lineW += 30;
					}
				}
			}
			f += s + "����";
		}
		f = f.replace("<br>", "\n");
		System.out.println(f);
	}
}
