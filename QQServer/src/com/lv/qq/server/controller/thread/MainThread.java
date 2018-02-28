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
			System.out.println("���ڶ˿ں�9998����");
			while(true){
				Socket socket = server.accept();
				//Ϊ���⴦����Ϣʱ��ɶ����ȴ�������һ���µ��߳�ȥ����
				new LoginThread(socket, controller).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
