package com.lv.qq.client.ui.login;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.client.util.SimpleButton;
import com.lv.qq.client.util.TransparentFrame;

public class LoginFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;
	
	private ImageIcon bgLogin = ImgIcon.BG_LOGIN;
	private ImageIcon btnLogin = ImgIcon.BTN_LOGIN;
	private ImageIcon btnCancer = ImgIcon.BTN_CANCER;
	private ImageIcon btnSetting = ImgIcon.BTN_SETTING;
	
	private JLabel photo = new JLabel(headPhoto);
	
	private JTextField account = new JTextField();
	private JPasswordField password = new JPasswordField();
	
	private JLabel regist = new LinkLable("注册账号");
	private JLabel findPsw = new LinkLable("找回密码");
	
	private JCheckBox remenber = new JCheckBox("记住密码");
	private JCheckBox autoLogin = new JCheckBox("自动登录");
	
	private SimpleButton btn_login = new SimpleButton(btnLogin);
	private SimpleButton btn_cancer = new SimpleButton(btnCancer);
	private SimpleButton btn_setting = new SimpleButton(btnSetting);
	
	private ClientControl control;
	
	@SuppressWarnings("deprecation")
	public LoginFrame(ClientControl control){
		super(SYSTEM_EXIT);
		this.control = control;
		//设置窗口大小
		this.setSize(430,330);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//设置是否可见 
		this.setVisible(false);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
	}
	
	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_minimize.setBounds(this.getWidth() - 60, 0, 30, 30);
		mainPanel.add(btn_close);
		mainPanel.add(btn_minimize);
		btn_setting.setBounds(340, 0, 30, 30);
		mainPanel.add(btn_setting);
		mainPanel.add(new JLabel(bgLogin), BorderLayout.NORTH);
		mainPanel.add(createLoginPanel(), BorderLayout.CENTER);
		return mainPanel;
	}

	private JPanel createLoginPanel() {
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(null);
		photo.setBounds(42, 12, 80, 80);
		loginPanel.add(photo);
		account.setBounds(134, 12, 194, 30);
		password.setBounds(134, 41, 194, 30);
		loginPanel.add(account);
		loginPanel.add(password);
		regist.setBounds(339, 12, 53, 30);
		regist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				control.createRegisterFrame();
			}
		});
		findPsw.setBounds(339, 41, 53, 30);
		loginPanel.add(regist);
		loginPanel.add(findPsw);
		remenber.setBounds(134, 79, 83, 20);
		autoLogin.setBounds(260, 79, 83, 20);
		Font font = new Font("宋体", Font.PLAIN, 12);
		remenber.setFont(font);
		autoLogin.setFont(font);
		loginPanel.add(remenber);
		loginPanel.add(autoLogin);
		btn_login.setBounds(134, 105, 194, 30);
		btn_cancer.setBounds(118, 105, 194, 30);
		btn_cancer.setVisible(false);
		btn_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String accontText = account.getText();
				String pwdText = String.valueOf(password.getPassword());
				if(accontText == null || "".equals(accontText.trim())){
					account.requestFocus();
					//userName.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				if(pwdText == null || "".equals(pwdText.trim())){
					password.requestFocus();
					//password.setBorder(BorderFactory.createLineBorder(Color.RED));
					return;
				}
				account.setVisible(false);
				password.setVisible(false);
				regist.setVisible(false);
				findPsw.setVisible(false);
				remenber.setVisible(false);
				autoLogin.setVisible(false);
				btn_login.setVisible(false);
				btn_cancer.setVisible(true);
				photo.setBounds(175, 12, 80, 80);
				control.login(Long.valueOf(accontText), pwdText, remenber.isSelected(), autoLogin.isSelected());
			}
		});
		btn_cancer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancer();
			}
		});
		loginPanel.add(btn_login);
		loginPanel.add(btn_cancer);
		return loginPanel;
	}
	
	public void cancer(){
		account.setVisible(true);
		account.setText("");
		password.setVisible(true);
		password.setText("");
		regist.setVisible(true);
		findPsw.setVisible(true);
		remenber.setVisible(true);
		autoLogin.setVisible(true);
		btn_login.setVisible(true);
		btn_cancer.setVisible(false);
		photo.setBounds(42, 12, 80, 80);
	}

	public void fillAccount(long act) {
		account.setText(String.valueOf(act));
	}

}
