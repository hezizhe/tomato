package com.lv.qq.server.controller.thread;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.lv.qq.common.vo.Message;
import com.lv.qq.server.controller.ServerController;

public class CommunicationThread extends Thread {
	
	private Socket socket;
	private ServerController controller;
	private Long account;

	public CommunicationThread(Socket socket, ServerController controller, Long account) {
		this.socket = socket;
		this.controller = controller;
		this.account = account;
	}
	
	public void sendMessage(Message message){
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void run() {
		while(true){
			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				Message message = (Message) ois.readObject();
				System.out.println("��ȡ����Ϣ��" + message.toString());
				Message reply = controller.dealWith(message);
				if(reply != null){
					System.out.println("������Ϣ��" + reply.toString());
					oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(reply);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("�û�" + account + "�Ѿ����ߣ�");
				break;
			} 
		}
		controller.outOfLine(account);
	}
	
}
