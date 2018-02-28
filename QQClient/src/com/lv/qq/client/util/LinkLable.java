package com.lv.qq.client.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class LinkLable extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private Font font = new Font("ËÎÌו", Font.PLAIN, 12);

	public LinkLable(String text){
		super(text);
		this.setFont(font);
		this.setForeground(new Color(38, 133, 227));
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				setForeground(new Color(38, 133, 227));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				setForeground(new Color(108, 198, 255));
			}
		});
	}
}
