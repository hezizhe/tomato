package com.lv.qq.common.vo;

import java.io.Serializable;
import java.util.List;

public class ContactGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	private int groupId;
	private String goupName;
	private List<Contact> contactList;
	
	public ContactGroup(){
	}
	
	public ContactGroup(String goupName, List<Contact> contactList) {
		this.goupName = goupName;
		this.contactList = contactList;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGoupName() {
		return goupName;
	}
	public void setGoupName(String goupName) {
		this.goupName = goupName;
	}
	public List<Contact> getContactList() {
		return contactList;
	}
	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}
}
