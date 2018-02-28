package com.lv.qq.client.ui.client;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.ui.ImgIcon;

public class GroupNameMouseListenner extends MouseAdapter {
	private JLabel arrowName;
	private JPanel contactListPanel;
	
	public GroupNameMouseListenner(JPanel contactListPanel, JLabel arrowName) {
		super();
		this.arrowName = arrowName;
		this.contactListPanel = contactListPanel;
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		((JPanel)e.getSource()).setBackground(null);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		((JPanel)e.getSource()).setBackground(new Color(202, 202, 202, 255));
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(arrowName.getIcon() == ImgIcon.ARROW_OPEN){
			arrowName.setIcon(ImgIcon.ARROW_CLOSE);
			contactListPanel.setVisible(false);
			return;
		}
		if(arrowName.getIcon() == ImgIcon.ARROW_CLOSE){
			arrowName.setIcon(ImgIcon.ARROW_OPEN);
			contactListPanel.setVisible(true);
			return;
		}
	}
}
