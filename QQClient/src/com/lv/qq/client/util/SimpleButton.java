package com.lv.qq.client.util;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.lv.qq.client.ui.ImgIcon;

public class SimpleButton extends JButton {

	private static final long serialVersionUID = 1L;
	
	private Color colorEnter = new Color(0, 188, 222, 255);
	private Color colorClose = new Color(226, 18, 18, 230);
	
	private boolean action = true;
	
	public SimpleButton(ImageIcon icon){
		super(icon);
		this.setBorder(null);
		this.setContentAreaFilled(false); 
		this.setBackground(new Color(0, 0, 0, 0));
		this.setFocusPainted(false);
		this.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				if(action)
					setSize(getWidth() - 2, getHeight() - 2);
				setContentAreaFilled(false);
			}
			public void mouseEntered(MouseEvent e) {
				if(action)
					setSize(getWidth() + 2, getHeight() + 2);
				setContentAreaFilled(true); 
				
				Icon icon = ((JButton)e.getSource()).getIcon();
				if(icon == ImgIcon.BTN_CLOSE) //|| icon == ImgIcon.CHAT_BTN_CLOSE)
					setBackground(colorClose);
				else
					setBackground(colorEnter);
			}
		});
	}

	public void setColorEnter(Color colorEnter) {
		this.colorEnter = colorEnter;
	}

	public void setColorClose(Color colorClose) {
		this.colorClose = colorClose;
	}

	public void setAction(boolean action) {
		this.action = action;
	}
	
}
