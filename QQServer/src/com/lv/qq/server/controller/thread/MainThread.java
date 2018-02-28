package com.lv.qq.server.controller.thread;

import java.net.ServerSocket;
import java.net.Socket;

import com.lv.qq.server.controller.ServerController;

public class MainThread extends Thread {
	
	private ServerController controller;
	
	public MainThread(ServerController controller){
		this.controller = controller;
	}

	public void run(){
		try {
			ServerSocket server = new ServerSocket(9998);
			System.out.println("正在端口号9998监听");
			while(true){
				Socket socket = server.accept();
				//为避免处理信息时造成堵塞等待，创建一个新的线程去处理
				new LoginThread(socket, controller).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
