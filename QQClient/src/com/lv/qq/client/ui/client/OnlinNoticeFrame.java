package com.lv.qq.client.ui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.Contact;

public class OnlinNoticeFrame extends TransparentFrame {
	private static final long serialVersionUID = 1L;
	
	private Contact contact;

	@SuppressWarnings("deprecation")
	public OnlinNoticeFrame(Contact cont) {
		super(FRAME_CLOSE);
		this.contact = cont;
		this.setSize(210, 130);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                this.getGraphicsConfiguration());
        int x = (int) (screen.getWidth() - 210 - 3);
        int y = (int) (screen.getHeight() - screenInsets.bottom - 130 - 3);
		this.setLocation(x, y);;
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
		new Reminding(this).start();
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.TITLE_BG.getImage(), 0, 0, 210, 30, 0, 0, 210, 30, null);
			}
		};
		mainPanel.setLayout(null);
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_close.setAction(false);
		mainPanel.add(btn_close);
		JLabel title = new JLabel();
		if(contact.getStatus() == 0){
			title.setText("下线提醒");
		}else{
			title.setText("上线提醒");
		}
		title.setBounds(30, 0, 70, 30);
		mainPanel.add(title);
		JLabel head = new JLabel();
		if(contact.getStatus() == 0){
			head.setIcon(ImgIcon.HEAD_PHOTO_OUTLINE_MINI);
		}else{
			head.setIcon(ImgIcon.HEAD_PHOTO_MINI);
		}
		head.setBounds(13, 46, 40, 40);
		mainPanel.add(head);
		LinkLable msg = new LinkLable(contact.getRemarks() + "<" + contact.getAccount() + ">");
		msg.setBounds(66, 46, 134, 20);
		mainPanel.add(msg);
		JLabel status = new JLabel();
		if(contact.getStatus() == 0){
			status.setText("下线了");
		}else{
			status.setText("上线了");
		}
		status.setFont(new Font("宋体", Font.PLAIN, 12));
		status.setBounds(66, 66, 134, 20);
		mainPanel.add(status);
		mainPanel.setBackground(Color.WHITE);
		//mainPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		return mainPanel;
	}
	
}

class Reminding extends Thread {
	private JFrame frame;
	public Reminding(JFrame frame){
		this.frame = frame;
	}
	
	public void run(){
		try {
			Thread.sleep(3000);
			frame.dispose();
			frame.setVisible(false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
