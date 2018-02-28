package com.lv.qq.client.ui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.util.TransparentFrame;

public class WelcomeFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	public WelcomeFrame() {
		super(SYSTEM_EXIT);
		//设置窗口大小
		this.setSize(310, 386);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//设置是否可见 
		this.setVisible(true);
		JPanel mainPanel = new JPanel();
		JLabel welcome = new JLabel(new ImageIcon("image/welcome.gif"));
		mainPanel.add(welcome);
		mainPanel.setBackground(new Color(0, 0, 0, 0));
		this.setContentPane(mainPanel);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
	}

}
