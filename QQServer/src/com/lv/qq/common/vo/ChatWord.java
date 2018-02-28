package com.lv.qq.common.vo;

import java.awt.Font;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ChatWord implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String word;
	private long talker;
	private long listener;
	private Font font;
	private Date time;
	private int type;
	private String filePath;
	private String fileName;
	private long fileLength;
	private String remarks;
	private String groupName;
	
	public ChatWord(){
	}
	
	public ChatWord(String word, long talker, long listener, Font font) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.word = word;
		this.talker = talker;
		this.listener = listener;
		this.font = font;
		this.time = new Date();
		this.type = 1;
	}
	
	public ChatWord(long talker, long listener, File file) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.talker = talker;
		this.listener = listener;
		this.time = new Date();
		this.type = 2;
		this.fileName = file.getName();
		this.fileLength = file.length();
		this.filePath = file.getParent();
	}
	
	public ChatWord(String remarks, String groupName, long talker, long listener) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.talker = talker;
		this.listener = listener;
		this.time = new Date();
		this.type = 3;
		this.remarks = remarks;
		this.groupName = groupName;
	}
	
	public ChatWord(long listener, String word) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.listener = listener;
		this.word = word;
		this.time = new Date();
		this.type = 4;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public long getTalker() {
		return talker;
	}
	public void setTalker(long talker) {
		this.talker = talker;
	}
	public long getListener() {
		return listener;
	}
	public void setListener(long listener) {
		this.listener = listener;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
