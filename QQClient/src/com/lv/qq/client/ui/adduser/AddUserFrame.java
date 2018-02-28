package com.lv.qq.client.ui.adduser;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.DateTools;
import com.lv.qq.client.util.InnerTextField;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.client.util.SimpleButton;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.User;

public class AddUserFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;
	
	private InnerTextField keyWord = new InnerTextField(" 请输入QQ号码/昵称/关键词/手机号/邮箱");
	private Choice locationChoice = new Choice();
	private Choice hometownChoice = new Choice();
	private Choice genderChoice = new Choice();
	private Choice ageChoice = new Choice();
	private JCheckBox online = new JCheckBox("在  线");
	private JCheckBox camera = new JCheckBox("摄像头");
	
	JPanel mainPanel = new JPanel(new BorderLayout());
	JPanel listPanel = new JPanel();
	
	private ClientControl control;
	private User user;
	private List<ContactGroup> contactGroups;
	
	@SuppressWarnings("deprecation")
	public AddUserFrame(ClientControl clientControl, User u, List<ContactGroup> groups) {
		super(FRAME_CLOSE);
		this.control = clientControl;
		this.user = u;
		this.contactGroups = groups;
		//设置窗口大小
		this.setSize(900,600);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//设置是否可见 
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
		Map<String, String> requirement = new HashMap<String, String>();
		requirement.put("account", String.valueOf(user.getAccount()));
		requirement.put("limit", "20");
		control.findUserList(requirement);
	}

	private JPanel createMainPanel() {
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_minimize.setBounds(this.getWidth() - 60, 0, 30, 30);
		mainPanel.add(btn_close);
		mainPanel.add(btn_minimize);
		mainPanel.add(createSearchPane(), BorderLayout.NORTH);
		mainPanel.add(createListPanel(), BorderLayout.CENTER);
		return mainPanel;
	}

	private JPanel createSearchPane() {
		JPanel searchPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.ADD_USER_TITLE_BG.getImage(), 0, 0, 900, 60, 0, 0, 900, 60, null);
			}
		};
		searchPanel.setLayout(null);
		JLabel findUser = new JLabel("找人");
		findUser.setFont(new Font("黑体", Font.BOLD, 16));
		findUser.setForeground(Color.WHITE);
		findUser.setBounds(210, 25, 45, 24);
		searchPanel.add(findUser);
		JLabel index = new JLabel("|");
		index.setFont(new Font("黑体", Font.PLAIN, 16));
		index.setForeground(Color.WHITE);
		index.setBounds(275, 25, 45, 24);
		searchPanel.add(index);
		JLabel findGroup = new JLabel("找群");
		findGroup.setFont(new Font("黑体", Font.PLAIN, 16));
		findGroup.setForeground(Color.WHITE);
		findGroup.setBounds(320, 25, 45, 24);
		searchPanel.add(findGroup);
		JLabel finger = new JLabel(ImgIcon.ADD_USER_FINGER);
		finger.setBounds(220, 54, 12, 6);
		searchPanel.add(finger);
		searchPanel.add(createUserSearchPanel());
		searchPanel.setBackground(new Color(235, 242, 249));
		searchPanel.setPreferredSize(new Dimension(900, 150));
		return searchPanel;
	}
	
	private JPanel createUserSearchPanel() {
		JPanel userSearchPanel = new JPanel();
		userSearchPanel.setLayout(null);
		keyWord.setBounds(18, 13, 478, 30);
		keyWord.setBorder(BorderFactory.createLineBorder(new Color(0, 155, 219)));
		userSearchPanel.add(keyWord);
		locationChoice.add("所在地：不限");
		for (String city : PersonalConfig.CITYS) {
			locationChoice.add("所在地：" + city);
		}
		locationChoice.setBounds(18, 53, 156, 25);
		userSearchPanel.add(locationChoice);
		hometownChoice.add("故乡：不限");
		for (String city : PersonalConfig.CITYS) {
			hometownChoice.add("故乡：" + city);
		}
		hometownChoice.setBounds(183, 53, 155, 25);
		userSearchPanel.add(hometownChoice);
		genderChoice.add("性别");
		for (Dictionary gender : PersonalConfig.GENDER_LIST) {
			genderChoice.add(gender.getName());
		}
		genderChoice.setBounds(348, 53, 70, 25);
		userSearchPanel.add(genderChoice);
		ageChoice.add("年龄");
		for (String age : PersonalConfig.AGES) {
			ageChoice.add(age);
		}
		ageChoice.setBounds(427, 53, 70, 25);
		userSearchPanel.add(ageChoice);
		online.setFont(new Font("黑体", Font.PLAIN, 13));
		online.setBounds(510, 19, 70, 15);
		userSearchPanel.add(online);
		camera.setFont(new Font("黑体", Font.PLAIN, 13));
		camera.setBounds(510, 54, 70, 15);
		userSearchPanel.add(camera);
		SimpleButton btnSearch = new SimpleButton(ImgIcon.BTN_SEARCH);
		btnSearch.setBounds(590, 24, 116, 39);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, String> requirement = new HashMap<String, String>();
				requirement.put("account", String.valueOf(user.getAccount()));
				requirement.put("limit", "20");
				String location = locationChoice.getSelectedItem().substring(4);
				String hometown = hometownChoice.getSelectedItem().substring(3);
				String genderName = genderChoice.getSelectedItem();
				String ageRange = ageChoice.getSelectedItem();
				String keyword = keyWord.getText();
				if(!location.equals("不限")){
					requirement.put("location", location);
				}
				if(!hometown.equals("不限")){
					requirement.put("hometown", hometown);
				}
				if(!genderName.equals("性别")){
					int genderValue = PersonalConfig.GENDER_NAME_MAP.get(genderName);
					requirement.put("gender", String.valueOf(genderValue));
				}
				if(online.isSelected()){
					requirement.put("status", String.valueOf(1));
				}
				if(!ageRange.equals("年龄")){
					if(ageRange.equals("18岁以下")){
						Date birthdayMin = DateTools.addYear(new Date(), -18);
						requirement.put("birthdayMin", DateTools.formatTime(birthdayMin, "yyyy-MM-dd HH:mm:ss"));
					}else if(ageRange.equals("18-22岁")){
						Date birthdayMax = DateTools.addYear(new Date(), -18);
						requirement.put("birthdayMax", DateTools.formatTime(birthdayMax, "yyyy-MM-dd HH:mm:ss"));
						Date birthdayMin = DateTools.addYear(new Date(), -23);
						requirement.put("birthdayMin", DateTools.formatTime(birthdayMin, "yyyy-MM-dd HH:mm:ss"));
					}else if(ageRange.equals("23-26岁")){
						Date birthdayMax = DateTools.addYear(new Date(), -23);
						requirement.put("birthdayMax", DateTools.formatTime(birthdayMax, "yyyy-MM-dd HH:mm:ss"));
						Date birthdayMin = DateTools.addYear(new Date(), -27);
						requirement.put("birthdayMin", DateTools.formatTime(birthdayMin, "yyyy-MM-dd HH:mm:ss"));
					}else if(ageRange.equals("27-35岁")){
						Date birthdayMax = DateTools.addYear(new Date(), -27);
						requirement.put("birthdayMax", DateTools.formatTime(birthdayMax, "yyyy-MM-dd HH:mm:ss"));
						Date birthdayMin = DateTools.addYear(new Date(), -36);
						requirement.put("birthdayMin", DateTools.formatTime(birthdayMin, "yyyy-MM-dd HH:mm:ss"));
					}else if(ageRange.equals("35岁以上")){
						Date birthdayMax = DateTools.addYear(new Date(), -36);
						requirement.put("birthdayMax", DateTools.formatTime(birthdayMax, "yyyy-MM-dd HH:mm:ss"));
					}
				}
				if(!keyword.equals(" 请输入QQ号码/昵称/关键词/手机号/邮箱") && !keyword.trim().equals("")){
					requirement.put("keyWord", keyword.trim());
				}
				control.findUserList(requirement);
			}
		});
		userSearchPanel.add(btnSearch);
		LinkLable sameCity = new LinkLable("同城交友");
		sameCity.setBounds(726, 34, 60, 19);
		userSearchPanel.add(sameCity);
		LinkLable sameHome = new LinkLable("同城老乡");
		sameHome.setBounds(786, 34, 60, 19);
		userSearchPanel.add(sameHome);
		userSearchPanel.setBackground(new Color(235, 242, 249));
		userSearchPanel.setPreferredSize(new Dimension(900, 90));
		userSearchPanel.setBounds(0, 60, 900, 90);
		return userSearchPanel;
	}

	private JPanel createListPanel() {
		listPanel.setLayout(null);
		listPanel.setBackground(Color.WHITE);
		listPanel.setPreferredSize(new Dimension(900, 450));
		return listPanel;
	}

	private JPanel createUserPanel(User u) {
		final User addUser = u;
		JPanel userPanel = new JPanel();
		userPanel.setLayout(null);
		userPanel.setBackground(Color.WHITE);
		JLabel headPhoto = new JLabel();
		if(addUser.getStatus() != 0){
			headPhoto.setIcon(ImgIcon.HEAD_PHOTO_MEDIUM);
		}else {
			headPhoto.setIcon(ImgIcon.HEAD_PHOTO_OUTLINE_MEDIUM);
		}
		headPhoto.setBounds(20, 15, 60, 60);
		userPanel.add(headPhoto);
		JLabel name = new JLabel(addUser.getUserName());
		name.setBounds(94, 15, 110, 14);
		userPanel.add(name);
		String infoText = "";
		if(addUser.getBirthday() != null){
			infoText = DateTools.countAge(addUser.getBirthday()) + "岁";
		}
		if(addUser.getLocation() != null && !addUser.getLocation().equals("")){
			infoText = infoText + "| " + addUser.getLocation();
		}
		JLabel info = new JLabel(infoText);
		info.setForeground(new Color(110, 110, 110));
		info.setFont(new Font("黑体", Font.PLAIN, 13));
		info.setBounds(94, 35, 110, 14);
		userPanel.add(info);
		SimpleButton btnAdd = new SimpleButton(ImgIcon.BTN_ADD_USER);
		btnAdd.setBounds(94, 55, 54, 20);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddConfirmFrame(control, user, addUser, contactGroups, null);
			}
		});
		userPanel.add(btnAdd);
		userPanel.setPreferredSize(new Dimension(225, 90));
		return userPanel;
	}
	
	public void refreshUserList(List<User> userList){
		listPanel.removeAll();
		for (int i = 0; i < userList.size(); i++) {
			JPanel userPanel = createUserPanel(userList.get(i));
			userPanel.setBounds((i % 4) * 225, (i / 4) * 90, 225, 90);
			listPanel.add(userPanel);
		}
		listPanel.repaint();
	}

}
