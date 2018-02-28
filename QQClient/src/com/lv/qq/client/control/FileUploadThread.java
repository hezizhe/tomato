package com.lv.qq.client.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.lv.qq.client.ui.chat.FileLoadPanel;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Message;

public class FileUploadThread extends LoadThread {
	
	private File file;
	
	public FileUploadThread(String ip, int port, ChatWord chatWord, File file, FileLoadPanel flp, ClientControl control) {
		this.ip = ip;
		this.port = port;
		this.chatWord = chatWord;
		this.file = file;
		this.flp = flp;
		this.control = control;
	}
	
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private FileInputStream fis = null;
	
	public void run(){
		try {
			socket = new Socket(ip, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(9, chatWord));
			oos.flush();
			dis = new DataInputStream(socket.getInputStream());
			String state = dis.readUTF();
			if(state.equals("start")){
				dos = new DataOutputStream(socket.getOutputStream());
				fis = new FileInputStream(file);
				byte[] b = new byte[1024];
				int length = 0;
				new ProgressCountThread(this, flp, chatWord.getFileLength()).start();
				while ((length = fis.read(b, 0, b.length)) != -1 && !cancer){
					dos.write(b, 0, length);
					dos.flush();
					progress += length;
					flp.setProgress(progress);
					flp.repaint();
				}
				if (!cancer) {
					control.transSuccess(chatWord.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResourse();
		}
	}
	
	public void closeResourse(){
		try {
			if(fis != null) fis.close();
			if(dos != null) dos.close();
			if(dis != null) dis.close();
			if(oos != null) oos.close();
			if(socket != null) socket.close();
			System.out.println("upload线程已关闭");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
