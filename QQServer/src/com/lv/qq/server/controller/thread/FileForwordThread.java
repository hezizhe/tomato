package com.lv.qq.server.controller.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.lv.qq.common.vo.ChatWord;

public class FileForwordThread extends Thread {
	private Socket senderSocket;
	private Socket accepterSocket;
	private ChatWord chatWord;
	private boolean cancer = false;
	
	public FileForwordThread(Socket senderSocket, ChatWord chatWord) {
		this.senderSocket = senderSocket;
		this.chatWord = chatWord;
	}

	public void setAccepterSocket(Socket accepterSocket) {
		this.accepterSocket = accepterSocket;
	}
	
	public void cancer(){
		cancer = true;
	}

	public void run(){
		DataOutputStream senderDos = null;
		DataOutputStream accepterDos = null;
		DataInputStream senderDis = null;
		try {
			senderDos = new DataOutputStream(senderSocket.getOutputStream());
			senderDos.writeUTF("start");
			senderDis = new DataInputStream(senderSocket.getInputStream());
			accepterDos = new DataOutputStream(accepterSocket.getOutputStream());
			byte[] b = new byte[1024];
			int length = 0;
			long progress = 0;
			while((length = senderDis.read(b, 0, b.length)) != -1 && !cancer){
				accepterDos.write(b, 0, length);
				accepterDos.flush();
				progress += length;
			}
			if(cancer){
				while(!isClientClose(senderSocket) || !isClientClose(accepterSocket)){
					Thread.sleep(1000);
				}
			}else {
				System.out.println("ת���ɹ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(senderDis != null) senderDis.close();
				if(accepterDos != null) accepterDos.close();
				if(senderDos != null) senderDos.close();
				System.out.println("ת���߳��ѹرգ�");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Boolean isClientClose(Socket socket) {
		try {
			socket.sendUrgentData(0);// ����1���ֽڵĽ������ݣ�Ĭ������£���������û�п����������ݴ�����Ӱ������ͨ��
			return false;
		} catch (Exception se) {
			return true;
		}
	}
}
