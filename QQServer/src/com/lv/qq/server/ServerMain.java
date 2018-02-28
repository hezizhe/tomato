package com.lv.qq.server;

import com.lv.qq.server.controller.ServerController;
import com.lv.qq.server.ui.ServerFrame;

public class ServerMain {
	public static void main(String[] args) {
		ServerController controller = new ServerController();
		new ServerFrame(controller);
	}
}
