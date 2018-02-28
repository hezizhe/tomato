package com.lv.qq.client.control;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.lv.qq.common.vo.Message;

public class CommunicationThread extends Thread {
	private Socket socket;
	private ClientControl control;
	
	public CommunicationThread(Socket socket, ClientControl control){
		this.socket = socket;
		this.control = control;
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
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				Message message = (Message) ois.readObject();
				System.out.println("读取到信息：" + message.toString());
				control.dealWith(message);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

}
