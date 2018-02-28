package com.lv.qq.server.dao;

public class ContactDto {
	private long account;
	private String remarks;
	private String groupName;
	private int groupId;
	
	
	public ContactDto() {
	}
	public ContactDto(long account, String remarks, String groupName) {
		this.account = account;
		this.remarks = remarks;
		this.groupName = groupName;
	}

	public long getAccount() {
		return account;
	}
	public void setAccount(long account) {
		this.account = account;
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
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
