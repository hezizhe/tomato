package com.lv.qq.client.control;

import com.lv.qq.client.ui.chat.FileLoadPanel;
import com.lv.qq.client.util.FileSizeFormatTool;

public class ProgressCountThread extends Thread {
	
	private long progress = 0;
	private LoadThread thread;
	private FileLoadPanel flp;
	private long fileLength;
	
	public ProgressCountThread(LoadThread thread, FileLoadPanel flp, long fileLength){
		this.thread = thread;
		this.flp = flp;
		this.fileLength = fileLength;
	}
	
	public void run(){
		try {
			while(true){
				long nowProgress = thread.getProgress();
				flp.setSpeed(FileSizeFormatTool.formatFileSize(nowProgress - progress) + "/S");
				this.progress = nowProgress;
				if(progress == fileLength){
					flp.setSpeed("0KB/S");
					break;
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
