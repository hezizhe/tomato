package com.lv.qq.server.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.lv.qq.server.controller.ServerController;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JButton btnStart = new JButton("Æô¶¯");
	
	private ServerController controller;

	public ServerFrame(ServerController controller){
		this.controller = controller;
		this.setSize(430,330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setContentPane(createMainPanel());
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel();
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.startMainThread();
				btnStart.setEnabled(false);
			}
		});
		mainPanel.add(btnStart);
		return mainPanel;
	}
}
