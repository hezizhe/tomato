package com.lv.qq.client.ui.chat;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.FileSizeFormatTool;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.common.vo.ChatWord;

public class FileLoadPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static Font font = new Font("宋体", Font.PLAIN, 11);
	
	public static int SEND = 1;
	public static int ACCEPT = 2;
	
	private int layout;
	private ChatWord chatWord;
	private ClientControl control;
	private ChatTab chatTab;
	private long progress;
	private FileLoadPanel thisFlp = this;
	
	JLabel speed = new JLabel("0KB/S");
	LinkLable accept = new LinkLable("接收");
	LinkLable cancer = new LinkLable("取消");;
	
	public FileLoadPanel(int lay, ChatWord word, ClientControl controller, ChatTab chatTab) {
		this.layout = lay;
		this.chatWord = word;
		this.control = controller;
		this.chatTab = chatTab;
		
		this.setLayout(null);
		JLabel fileLogo = new JLabel(ImgIcon.FILE_LOGO);
		fileLogo.setBounds(5, 5, 40, 40);
		this.add(fileLogo);
		JLabel fileLoad;
		if (this.layout == SEND) {
			fileLoad = new JLabel(ImgIcon.FILE_UPLOAD);
		} else {
			fileLoad = new JLabel(ImgIcon.FILE_DOWNLOAD);
		}
		fileLoad.setBounds(50, 5, 13, 11);
		this.add(fileLoad);
		JLabel fileName = new JLabel(chatWord.getFileName() + "(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength()) + ")");
		fileName.setBounds(65, 5, 160, 15);
		fileName.setFont(font);
		this.add(fileName);
		cancer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cancer.setVisible(false);
				control.requstCancerTrans(chatWord);
			}
		});
		cancer.setBounds(200, 32, 24, 14);
		this.add(cancer);
		if (this.layout == SEND) {
			LinkLable toOutline = new LinkLable("转离线发送");
			toOutline.setBounds(130, 32, 60, 14);
			this.add(toOutline);
		} else {
			accept.setBounds(166, 32, 24, 14);
			accept.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					accept.setVisible(false);
					control.acceptFile(chatWord, thisFlp);
				}
			});
			this.add(accept);
		}
		speed.setFont(font);
		speed.setBounds(50, 32, 100, 14);
		this.add(speed);
		speed.setVisible(false);
		this.setPreferredSize(new Dimension(230, 50));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawProgress(g);
	}

	private void drawProgress(Graphics g){
		g.setColor(new Color(200, 202, 205));
		g.fillRect(50, 25, 175, 3);
		int progressBar = (int) (175 * progress / chatWord.getFileLength());
		g.setColor(new Color(73, 191, 244));
		g.fillRect(50, 25, progressBar, 2);
		g.setColor(new Color(54, 165, 226));
		g.fillRect(50, 27, progressBar, 1);
	}

	public void setProgress(long progress) {
		this.progress = progress;
	}
	
	public void setSpeed(String speed){
		this.speed.setVisible(true);
		this.speed.setText(speed);
	}

	public void transSuccess() {
		this.chatTab.transSuccess(chatWord);
	}

	public void cancerTrans(long breaker, long progress) {
		this.chatTab.cancerTrans(chatWord, breaker, progress);
	}

}
