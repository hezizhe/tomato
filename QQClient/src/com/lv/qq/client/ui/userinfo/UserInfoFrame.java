package com.lv.qq.client.ui.userinfo;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.DateTools;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.User;

public class UserInfoFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;
	
	private ClientControl clientControl;
	
	private User user;
	
	private JTextField userName = new JTextField();
	private Choice gender = new Choice();
	private JTextField birthday = new JTextField();
	private Choice bloodType = new Choice();
	private Choice profession = new Choice();
	private Choice hometown = new Choice();
	private Choice location = new Choice();
	private JTextField school = new JTextField();
	private JTextField company = new JTextField();
	private JTextField phone = new JTextField();
	private JTextField email = new JTextField();
	private JTextArea signature = new JTextArea();
	private JTextArea introduce = new JTextArea();
	private JButton btnClose = new JButton("关闭");
	private JButton btnSave = new JButton("保存");
	
	@SuppressWarnings("deprecation")
	public UserInfoFrame(ClientControl control, User user) {
		super(FRAME_CLOSE);
		this.clientControl = control;
		this.user = user;
		this.setSize(376, 560);
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
		JLabel title = new JLabel("编辑资料");
		title.setBounds(30, 0, 70, 30);
		mainPanel.add(title);
		addLable(mainPanel, "账   号", 12, 43);
		JLabel account = new JLabel(String.valueOf(user.getAccount()));
		account.setBounds(73, 43, 100, 22);
		account.setForeground(Color.RED);
		mainPanel.add(account);
		hometown.add("");
		location.add("");
		for (String city : PersonalConfig.CITYS) {
			hometown.add(city);
			location.add(city);
		}
		if (user.getHometown() == null || user.getHometown().trim().equals("")) {
			hometown.select("");
		} else {
			hometown.select(user.getHometown());
		}
		if (user.getLocation() == null || user.getLocation().trim().equals("")) {
			location.select("");
		} else {
			location.select(user.getLocation());
		}
		
		addLable(mainPanel, "昵   称", 12, 79);
		userName.setText(user.getUserName());
		addField(mainPanel, userName, 73, 79, 102, 22);
		addLable(mainPanel, "性   别", 200, 79);
		for (Dictionary gen : PersonalConfig.GENDER_LIST) {
			gender.add(gen.getName());
		}
		gender.select(PersonalConfig.GENDER_VALUE_MAP.get(user.getGender()));
		addField(mainPanel, gender, 261, 79, 102, 22);
		addLable(mainPanel, "生   日", 12, 114);
		if(user.getBirthday() != null)
			birthday.setText(DateTools.formatTime(user.getBirthday(), "yyyy-MM-dd"));
		addField(mainPanel, birthday, 73, 114, 102, 22);
		addLable(mainPanel, "血   型", 200, 114);
		for (Dictionary blood : PersonalConfig.BLOOD_LIST) {
			bloodType.add(blood.getName());
		}
		bloodType.select(PersonalConfig.BLOOD_VALUE_MAP.get(user.getBloodType()));
		addField(mainPanel, bloodType, 261, 114, 102, 22);
		addLable(mainPanel, "职   业", 12, 149);
		for (Dictionary pro : PersonalConfig.PROFESSION_LIST) {
			profession.add(pro.getName());
		}
		profession.select(PersonalConfig.PROFESSION_VALUE_MAP.get(user.getProfession()));
		addField(mainPanel, profession, 73, 149, 290, 22);
		addLable(mainPanel, "家   乡", 12, 184);
		addField(mainPanel, hometown, 73, 184, 290, 22);
		addLable(mainPanel, "所在地", 12, 219);
		addField(mainPanel, location, 73, 219, 290, 22);
		addLable(mainPanel, "学   校", 12, 254);
		school.setText(user.getSchool());
		addField(mainPanel, school, 73, 254, 290, 22);
		addLable(mainPanel, "公   司", 12, 289);
		company.setText(user.getCompany());
		addField(mainPanel, company, 73, 289, 290, 22);
		addLable(mainPanel, "手   机", 12, 324);
		phone.setText(user.getPhone());
		addField(mainPanel, phone, 73, 324, 290, 22);
		addLable(mainPanel, "邮   箱", 12, 359);
		email.setText(user.getEmail());
		addField(mainPanel, email, 73, 359, 290, 22);
		addLable(mainPanel, "个性签名", 12, 394);
		signature.setText(user.getSignature());
		signature.setLineWrap(true);
		addField(mainPanel, signature, 73, 394, 290, 46);
		addLable(mainPanel, "个人说明", 12, 453);
		introduce.setText(user.getIntroduce());
		introduce.setLineWrap(true);
		addField(mainPanel, introduce, 73, 453, 290, 46);
		mainPanel.add(createBtnPanel());
		mainPanel.setBackground(Color.WHITE);
		return mainPanel;
	}
	
	private JPanel createBtnPanel() {
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(null);
		btnPanel.setBackground(new Color(221, 221, 221));
		btnPanel.setBounds(0, 516, 376, 44);
		btnSave.setFont(new Font("Default", 0, 13));
		btnSave.setBounds(224, 10, 64, 24);
		btnSave.setBackground(new Color(244, 244, 244));
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(userName.getText().trim().equals("")){
					userName.requestFocus();
					return;
				}
				btnSave.setEnabled(false);
				btnClose.setEnabled(false);
				user.setUserName(userName.getText().trim());
				user.setGender(PersonalConfig.GENDER_NAME_MAP.get(gender.getSelectedItem()));
				user.setBirthday(DateTools.getDateByString(birthday.getText(), "yyyy-MM-dd"));
				user.setBloodType(PersonalConfig.BLOOD_NAME_MAP.get(bloodType.getSelectedItem()));
				user.setProfession(PersonalConfig.PROFESSION_NAME_MAP.get(profession.getSelectedItem()));
				user.setHometown(hometown.getSelectedItem());
				user.setLocation(location.getSelectedItem());
				user.setSchool(school.getText().trim());
				user.setCompany(company.getText().trim());
				user.setPhone(phone.getText().trim());
				user.setEmail(email.getText().trim());
				user.setSignature(signature.getText().trim());
				user.setIntroduce(introduce.getText().trim());
				clientControl.perfectUserInfo(user);
			}
		});
		btnPanel.add(btnSave);
		btnClose.setFont(new Font("Default", 0, 13));
		btnClose.setBounds(300, 10, 64, 24);
		btnClose.setBackground(new Color(244, 244, 244));
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnPanel.add(btnClose);
		return btnPanel;
	}

	private void addLable(JPanel mainPanel, String title, int x, int y){
		JLabel lable = new JLabel(title);
		lable.setBounds(x, y, 60, 22);
		lable.setFont(new Font("Default", 0, 13));
		lable.setForeground(new Color(127, 127, 127));
		mainPanel.add(lable);
	}
	
	private void addField(JPanel mainPanel, Component filed, int x, int y, int w, int h){
		filed.setBounds(x, y, w, h);
		if(!(filed instanceof Choice)){
			((JComponent) filed).setBorder(BorderFactory.createLineBorder(new Color(173, 173, 173)));
		}
		mainPanel.add(filed);
	}

	public void showResult(boolean updateSuccess) {
		// TODO Auto-generated method stub
	}

}
