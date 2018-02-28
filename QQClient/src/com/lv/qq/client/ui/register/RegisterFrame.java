package com.lv.qq.client.ui.register;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.InnerPasswordField;
import com.lv.qq.client.util.InnerTextField;
import com.lv.qq.client.util.SimpleButton;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.User;

public class RegisterFrame extends TransparentFrame {
	
	private static final long serialVersionUID = 1L;
	
	private InnerTextField userName = new InnerTextField(" 昵称");
	private InnerPasswordField password = new InnerPasswordField(" 密码");
	private InnerPasswordField repass = new InnerPasswordField(" 确认密码");
	private SimpleButton btnRegist = new SimpleButton(ImgIcon.BTN_REGISTER);
	private JLabel tip = new JLabel();
	private JLabel loading = new JLabel(ImgIcon.LOADING);
	
	private ClientControl clientControl;

	@SuppressWarnings("deprecation")
	public RegisterFrame(ClientControl control) {
		super(FRAME_CLOSE);
		this.clientControl = control;
		this.setSize(376, 376);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.TITLE_BG.getImage(), 0, 0, 376, 30, 0, 0, 376, 30, null);
			}
		};
		mainPanel.setLayout(null);
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_minimize.setBounds(this.getWidth() - 60, 0, 30, 30);
		btn_minimize.setColorEnter(new Color(210, 208, 190));
		btn_close.setAction(false);
		btn_minimize.setAction(false);
		mainPanel.add(btn_close);
		mainPanel.add(btn_minimize);
		JLabel title = new JLabel("用户注册");
		title.setBounds(30, 0, 70, 30);
		mainPanel.add(title);
		userName.setBounds(19, 50, 338, 50);
		userName.setFont(new Font("Default", 0, 20));
		mainPanel.add(userName);
		password.setBounds(19, 120, 338, 50);
		password.setFont(new Font("Default", 0, 20));
		mainPanel.add(password);
		repass.setBounds(19, 190, 338, 50);
		repass.setFont(new Font("Default", 0, 20));
		mainPanel.add(repass);
		tip.setFont(new Font("Default", 0, 14));
		tip.setBounds(19, 270, 338, 30);
		tip.setForeground(Color.RED);
		mainPanel.add(tip);
		loading.setBounds(156, 156, 64, 64);
		mainPanel.add(loading);
		loading.setBounds(156, 243, 64, 64);
		loading.setVisible(false);
		mainPanel.add(loading);
		btnRegist.setBounds(19, 310, 338, 50);
		btnRegist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = userName.getText().trim();
				String pass = String.valueOf(password.getPassword());
				String rePass = String.valueOf(repass.getPassword());
				if (name.equals("") || name.equals("昵称")) {
					tip.setText("");
					userName.requestFocus();
					return;
				} else if (pass.equals("") || pass.equals(" 密码")) {
					tip.setText("");
					password.requestFocus();
					return;
				} else if (rePass.equals("") || rePass.equals(" 确认密码")) {
					tip.setText("");
					repass.requestFocus();
					return;
				} else if (pass.indexOf(" ") != -1) {
					password.requestFocus();
					tip.setText("密码不能包含空格");
				} else if (!pass.equals(rePass)) {
					repass.requestFocus();
					tip.setText("两次密码不一致");
				} else {
					userName.setEnabled(false);
					password.setEnabled(false);
					repass.setEnabled(false);
					btnRegist.setEnabled(false);
					tip.setText("");
					loading.setVisible(true);
					User user = new User();
					user.setUserName(name);
					user.setPassword(pass);
					clientControl.register(user);
				}
			}
		});
		mainPanel.add(btnRegist);
		return mainPanel;
	}
	
}
