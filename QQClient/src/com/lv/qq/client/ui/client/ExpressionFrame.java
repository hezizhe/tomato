package com.lv.qq.client.ui.client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.TransparentFrame;

public class ExpressionFrame extends TransparentFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea chatEdit;

	@SuppressWarnings("deprecation")
	public ExpressionFrame(JTextArea chatEdit) {
		super(FRAME_CLOSE);
		this.chatEdit = chatEdit;
		this.setSize(240, 120);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.INFORM.getImage());
		this.show();
		this.setVisible(false);
		chatEdit.requestFocus();
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new GridLayout(4, 8, 0, 0));
		File root = new File("C:/Users/Administrator/Desktop/expression");
		File[] files = root.listFiles();
		for (File file : files) {
			JLabel gif = new JLabel(new ImageIcon(file.getPath()));
			String fileName = file.getName();
			final String expName = fileName.substring(0, fileName.length() - 4);
			gif.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String text = chatEdit.getText();
					chatEdit.setText(text + "</" + expName + ">");
					chatEdit.requestFocus();
				}
			});
			mainPanel.add(gif);
		}
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		return mainPanel;
	}
	
}
