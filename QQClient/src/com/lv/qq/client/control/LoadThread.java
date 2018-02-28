package com.lv.qq.client.control;

import com.lv.qq.client.ui.chat.FileLoadPanel;
import com.lv.qq.common.vo.ChatWord;

public abstract class LoadThread extends Thread {

	protected String ip;
	protected int port;
	protected ChatWord chatWord;
	protected FileLoadPanel flp;
	protected long progress = 0;
	protected ClientControl control;
	protected boolean cancer = false;
	
	public long getProgress() {
		return progress;
	}

	public void cancer() {
		System.out.println("传输已取消！");
		this.cancer = true;
	}
	
	public abstract void closeResourse();
}
