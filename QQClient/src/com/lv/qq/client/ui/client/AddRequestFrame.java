package com.lv.qq.client.ui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.ChatWord;

public class AddRequestFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;
	
	private ChatWord chatWord;
	private ClientControl clientControl;

	@SuppressWarnings("deprecation")
	public AddRequestFrame(ClientControl control, ChatWord word) {
		super(FRAME_CLOSE);
		this.chatWord = word;
		this.clientControl = control;
		this.setSize(210, 130);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                this.getGraphicsConfiguration());
        int x = (int) (screen.getWidth() - 210 - 3);
        int y = (int) (screen.getHeight() - screenInsets.bottom - 130 - 3);
		this.setLocation(x, y);;
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.INFORM.getImage());
		this.show();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				clientControl.hadRead(chatWord.getTalker());
			}
		});
		new AddReqReminding(this).start();
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(new Color(230, 230, 230));
				g.fillRect(0, 0, 210, 30);
				g.drawImage(ImgIcon.INFORM.getImage(), 7, 7, 23, 23, 0, 0, 16, 16, null);
				g.fillRect(0, 100, 210, 1);
			}
		};
		mainPanel.setLayout(null);
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_close.setAction(false);
		mainPanel.add(btn_close);
		JLabel title = new JLabel("验证消息");
		title.setBounds(30, 0, 70, 30);
		mainPanel.add(title);
		JLabel msg = new JLabel("您有一条好友添加消息！");
		msg.setFont(new Font("default", 0, 13));
		msg.setBounds(30, 45, 160, 30);
		mainPanel.add(msg);
		LinkLable check = new LinkLable("查看");
		check.setBounds(130, 100, 40, 30);
		check.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clientControl.hadRead(chatWord.getTalker());
				clientControl.checkAddRequest(chatWord);
				setVisible(false);
				dispose();
			}
		});
		mainPanel.add(check);
		LinkLable ignore = new LinkLable("忽略");
		ignore.setBounds(170, 100, 40, 30);
		ignore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clientControl.hadRead(chatWord.getTalker());
				setVisible(false);
				dispose();
			}
		});
		mainPanel.add(ignore);
		mainPanel.setBackground(Color.WHITE);
		return mainPanel;
	}
	
}

class AddReqReminding extends Thread {
	private JFrame frame;
	
	public AddReqReminding(JFrame frame){
		this.frame = frame;
	}
	
	public void run(){
		while(true){
			try {
				if(!frame.isVisible()){
					break;
				}
				if (frame.getIconImage() == ImgIcon.INFORM.getImage()){
					frame.setIconImage(ImgIcon.NULL.getImage());
					Thread.sleep(260);
				} else {
					frame.setIconImage(ImgIcon.INFORM.getImage());
					Thread.sleep(400);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
