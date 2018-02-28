package com.lv.qq.client.ui.adduser;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.DateTools;
import com.lv.qq.client.util.InnerTextField;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.User;

public class AddConfirmFrame extends TransparentFrame {
	
	private static final long serialVersionUID = 1L;
	private ClientControl clientControl;
	private User self;
	private User user;

	private Font font = new Font("default", 0, 13);
	
	private InnerTextField remark = new InnerTextField(" 选填");
	private Choice group = new Choice();
	private List<ContactGroup> contactGroups;
	
	private JButton btnNext = new JButton("下一步");
	private JButton btnCancel = new JButton("取消");
	
	private ChatWord chatWord;
	
	private JPanel markPanel = new JPanel(null);
	
	@SuppressWarnings("deprecation")
	public AddConfirmFrame(ClientControl control, User user, User addUser, List<ContactGroup> contactGroups, ChatWord chatWord) {
		super(FRAME_CLOSE);
		this.clientControl = control;
		this.self = user;
		this.user = addUser;
		this.contactGroups = contactGroups;
		this.chatWord = chatWord;
		this.setSize(460, 360);
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
				g.drawImage(ImgIcon.TITLE_BG.getImage(), 0, 0, 460, 30, 0, 0, 460, 30, null);
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
		JLabel title = new JLabel("添加好友");
		title.setBounds(30, 0, 70, 30);
		mainPanel.add(title);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.add(createInfoPanel());
		mainPanel.add(createMarkPanel());
		mainPanel.add(createBtnPanel());
		return mainPanel;
	}

	private JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel(null);
		JLabel head = new JLabel(new ImageIcon(ImgIcon.HEAD_PHOTO.getImage().getScaledInstance(108, 108,  
	            Image.SCALE_DEFAULT)));
		head.setBounds(10, 35, 108, 108);
		infoPanel.add(head);
		JLabel name = new JLabel(user.getUserName());
		name.setBounds(10, 148, 108, 20);
		infoPanel.add(name);
		JLabel account = new JLabel(String.valueOf(user.getAccount()));
		account.setBounds(10, 168, 108, 20);
		infoPanel.add(account);
		JLabel gender = new JLabel("性别：" + PersonalConfig.GENDER_VALUE_MAP.get(user.getGender()));
		gender.setForeground(new Color(114, 114, 114));
		gender.setBounds(10, 188, 108, 20);
		gender.setFont(font);
		infoPanel.add(gender);
		JLabel age = new JLabel("年龄：" + DateTools.countAge(user.getBirthday()) + "岁");
		age.setForeground(new Color(114, 114, 114));
		age.setBounds(10, 208, 108, 20);
		age.setFont(font);
		infoPanel.add(age);
		JLabel location = new JLabel("所在地：" + user.getLocation());
		location.setForeground(new Color(114, 114, 114));
		location.setBounds(10, 228, 108, 20);
		location.setFont(font);
		infoPanel.add(location);
		infoPanel.setBounds(0, 30, 128, 295);
		infoPanel.setBackground(new Color(252, 252, 251));
		return infoPanel;
	}
	
	private JPanel createMarkPanel() {
		JLabel remarkLab = new JLabel("备注姓名：");
		remarkLab.setFont(font);
		remarkLab.setBounds(18, 16, 65, 20);
		markPanel.add(remarkLab);
		remark.setBounds(83, 16, 168, 20);
		markPanel.add(remark);
		JLabel groupLab = new JLabel("分      组：");
		groupLab.setFont(font);
		groupLab.setBounds(18, 46, 65, 20);
		markPanel.add(groupLab);
		for (ContactGroup contactGroup : contactGroups) {
			group.add(contactGroup.getGoupName());
		}
		if(contactGroups.size() == 0){
			group.add("我的好友");
		}
		group.setBounds(83, 46, 168, 20);
		markPanel.add(group);
		markPanel.setBounds(128, 30, 332, 295);
		markPanel.setBackground(Color.WHITE);
		return markPanel;
	}
	
	private JPanel createBtnPanel() {
		JPanel btnPanel = new JPanel(null);
		btnPanel.setBounds(0, 325, 460, 35);
		btnNext.setFont(font);
		btnNext.setBounds(302, 7, 69, 24);
		btnNext.setBackground(new Color(244, 244, 244));
		btnNext.setBorder(BorderFactory.createLineBorder(new Color(79, 173, 216)));
		if(chatWord != null){
			btnNext.setText("完成");
		}
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnNext.setEnabled(false);
				btnCancel.setEnabled(false);
				String remarkName = remark.getText().trim();
				if(remarkName.equals("选填")){
					remarkName = user.getUserName();
				}
				String groupName = group.getSelectedItem();
				ChatWord cWord = new ChatWord(remarkName, groupName, self.getAccount(), user.getAccount());
				if (chatWord == null) {
					clientControl.sendWord(cWord);
					sendSuccess();
				} else {
					clientControl.completeFriends(chatWord, cWord);
					setVisible(false);
					dispose();
				}
			}
		});
		btnPanel.add(btnNext);
		btnCancel.setFont(font);
		btnCancel.setBounds(379, 7, 69, 24);
		btnCancel.setBackground(new Color(244, 244, 244));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnPanel.add(btnCancel);
		btnPanel.setBackground(new Color(247, 247, 246));
		return btnPanel;
	}
	
	private void sendSuccess(){
		btnNext.setVisible(false);
		btnCancel.setText("完成");
		markPanel.removeAll();
		JLabel success = new JLabel(ImgIcon.SUCCESS_LOGO);
		success.setBounds(20, 16, 30, 30);
		markPanel.add(success);
		JLabel msg = new JLabel("<html><body>你的好友添加请求已经发送成功，正在等待<br>对方确认。</body></html>");
		msg.setFont(font);
		msg.setBounds(60, 16, 252, 40);
		markPanel.add(msg);
		markPanel.repaint();
		btnCancel.setEnabled(true);
	}
}
