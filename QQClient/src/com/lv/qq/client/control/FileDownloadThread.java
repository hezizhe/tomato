package com.lv.qq.client.control;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.ui.chat.FileLoadPanel;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Message;

public class FileDownloadThread extends LoadThread {

	
	public FileDownloadThread(ChatWord chatWord, String ip, int port, FileLoadPanel flp, ClientControl control) {
		this.chatWord = chatWord;
		this.ip = ip;
		this.port = port;
		this.flp = flp;
		this.control = control;
	}
	
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private DataInputStream dis = null;
	private FileOutputStream fos = null;
	private File file;
	
	public void run(){
		try {
			socket = new Socket(ip, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(11, chatWord.getId()));
			oos.flush();
			dis = new DataInputStream(socket.getInputStream());
			file = new File(PersonalConfig.DOWNLOAD_PATH + "/" + chatWord.getFileName());
			fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int length = 0;
			new ProgressCountThread(this, flp, chatWord.getFileLength()).start();
			while((length = dis.read(b, 0, b.length)) != -1 && !cancer){
				fos.write(b, 0, length);
				fos.flush();
				progress += length;
				flp.setProgress(progress);
				flp.repaint();
			}
			if (!cancer) {
				chatWord.setFilePath(PersonalConfig.DOWNLOAD_PATH);
				control.transSuccess(chatWord.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResourse();
		}
	}
	
	public void closeResourse(){
		try {
			if (cancer && file != null) {
				file.delete();
			}
			if(fos != null) fos.close();
			if(dis != null) dis.close();
			if(oos != null) oos.close();
			if(socket != null && !socket.isClosed()) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
