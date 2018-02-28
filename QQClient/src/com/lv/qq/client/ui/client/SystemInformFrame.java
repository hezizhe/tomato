package com.lv.qq.client.ui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.TransparentFrame;

public class SystemInformFrame extends TransparentFrame {
	private static final long serialVersionUID = 1L;
	
	private String word;
	
	@SuppressWarnings("deprecation")
	public SystemInformFrame(String word) {
		super(FRAME_CLOSE);
		this.word = word;
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
		new Reminding(this).start();
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
		JLabel title = new JLabel("ϵͳ֪ͨ");
		title.setBounds(30, 0, 70, 30);
		mainPanel.add(title);
		JLabel msg = new JLabel(word);
		msg.setFont(new Font("default", 0, 13));
		msg.setBounds(30, 45, 160, 30);
		mainPanel.add(msg);
		mainPanel.setBackground(Color.WHITE);
		return mainPanel;
	}
}
