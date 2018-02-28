package com.lv.qq.client.ui.login;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.client.util.TransparentFrame;

public class LoginFailFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;
	
	private ClientControl control;

	@SuppressWarnings("deprecation")
	public LoginFailFrame(ClientControl control) {
		super(SYSTEM_EXIT);
		this.control = control;
		//设置窗口大小
		this.setSize(430,330);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//设置是否可见 
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
	}

	private Container createMainPanel() {
		JPanel mainPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.BG_LOGIN_FAIL.getImage(), 0, 0, 430, 330, null);
			}
		};
		mainPanel.setLayout(null);
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_minimize.setBounds(this.getWidth() - 60, 0, 30, 30);
		mainPanel.add(btn_close);
		mainPanel.add(btn_minimize);
		LinkLable findPsw = new LinkLable("找回密码");
		findPsw.setBounds(327, 196, 90, 30);
		mainPanel.add(findPsw);
		LinkLable clickHere = new LinkLable("点击这里");
		clickHere.setBounds(261, 232, 90, 30);
		mainPanel.add(clickHere);
		JLabel message = new JLabel();
		message.setBackground(null);
		String messageText = "<html><body>你输入的账户名或密码不正确，原因可能是：<br>1、帐号名输入有误；<br>" 
				+ "2、忘记密码；<br>3、未区分字母大小写；<br>4、未开启小键盘；<br>5、账户名未绑定QQ号。<br><br>"
				+ "如果你的密码丢失或遗忘，可访问QQ安全中心&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;。<br><br>"
				+ "如果你的账号为非大陆手机号，请&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;进行登录。</body></html>";
		message.setText(messageText);
		message.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		message.setBackground(new Color(235, 242, 249, 0));
		message.setBounds(80, 75, 320, 180);
		//message.setEditable(false);
		mainPanel.add(message);
		JButton btnOk = new JButton("确定");
		btnOk.setBounds(340, 303, 80, 24);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				control.backLogin();
			}
		});
		mainPanel.add(btnOk);
		return mainPanel;
	}
}
