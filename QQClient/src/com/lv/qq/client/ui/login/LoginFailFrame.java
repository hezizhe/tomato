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
		//���ô��ڴ�С
		this.setSize(430,330);
		//���ô��ھ���
		this.setLocationRelativeTo(null);
		//�����Ƿ�ɼ� 
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
		LinkLable findPsw = new LinkLable("�һ�����");
		findPsw.setBounds(327, 196, 90, 30);
		mainPanel.add(findPsw);
		LinkLable clickHere = new LinkLable("�������");
		clickHere.setBounds(261, 232, 90, 30);
		mainPanel.add(clickHere);
		JLabel message = new JLabel();
		message.setBackground(null);
		String messageText = "<html><body>��������˻��������벻��ȷ��ԭ������ǣ�<br>1���ʺ�����������<br>" 
				+ "2���������룻<br>3��δ������ĸ��Сд��<br>4��δ����С���̣�<br>5���˻���δ��QQ�š�<br><br>"
				+ "���������붪ʧ���������ɷ���QQ��ȫ����&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��<br><br>"
				+ "�������˺�Ϊ�Ǵ�½�ֻ��ţ���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���е�¼��</body></html>";
		message.setText(messageText);
		message.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		message.setBackground(new Color(235, 242, 249, 0));
		message.setBounds(80, 75, 320, 180);
		//message.setEditable(false);
		mainPanel.add(message);
		JButton btnOk = new JButton("ȷ��");
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
