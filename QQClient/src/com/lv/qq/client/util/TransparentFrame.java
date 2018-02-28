package com.lv.qq.client.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.lv.qq.client.ui.ImgIcon;

public class TransparentFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	protected ImageIcon btnClose = ImgIcon.BTN_CLOSE;
	protected ImageIcon btnMinimize = ImgIcon.BTN_MINIMIZE;
	protected ImageIcon headPhoto = ImgIcon.HEAD_PHOTO;
	protected ImageIcon headPhotoMini = ImgIcon.HEAD_PHOTO_MINI;
	
	protected SimpleButton btn_close = new SimpleButton(btnClose);
	protected SimpleButton btn_minimize = new SimpleButton(btnMinimize);
	
	private int locationX;
	private int locationY;
	
	public static int SYSTEM_EXIT = 0;
	public static int FRAME_CLOSE = 1;
	
	private int closeType;

	public TransparentFrame(int closeType){
		this.closeType = closeType;
		//去掉边框和标题框
		this.setUndecorated(true);
		//设置默认关闭
		if(closeType == SYSTEM_EXIT){
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}else if (closeType == FRAME_CLOSE) {
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		//实现窗口可拖拽
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				locationX = e.getX();
				locationY = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int devX = e.getX() - locationX;
				int devY = e.getY() - locationY;
				setLocation(getX() + devX, getY() + devY);
			}
		});
		//初始化最小化和关闭按钮事件监听
		btn_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeFrame();
			}
		});
		btn_minimize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setExtendedState(ICONIFIED);
			}
		});
	}
	
	private void closeFrame(){
		if(closeType == SYSTEM_EXIT)
			System.exit(0);
		if(closeType == FRAME_CLOSE){
			this.setVisible(false);
			this.dispose();
		}
	}
}
