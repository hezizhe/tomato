package com.lv.qq.client.ui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;

public class ContactGroupPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ClientControl clientControl;
	
	private Map<Long, ContactPanel> contactPanelMap = new HashMap<Long, ContactPanel>();
	
	private List<Contact> contactList;
	
	private String groupName;
	
	private JLabel arrowName;
	
	private JPanel contactListPanel;
	
	private boolean hasUnreadRecord = false;
	
	private GroupReminding REMINDING;
	
	/**
	 * 创建联系人分组面板
	 * @param groupName   联系人分组名称
	 * @param contactList 联系人列表
	 * @return 联系人分组面板
	 */
	public ContactGroupPanel(String groupName, List<Contact> contactList, ClientControl clientControl){
		this.clientControl = clientControl;
		this.contactList = contactList;
		this.groupName = groupName;
		this.setLayout(new BorderLayout());
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		namePanel.setPreferredSize(new Dimension(290, 27));
		int online = 0;
		for (Contact contact : contactList) {
			if(contact.getStatus() != 0)
				online++;
		}
		arrowName = new JLabel(groupName + "（" + online + "/" + contactList.size() + "）", ImgIcon.ARROW_CLOSE, JLabel.LEFT);
		namePanel.add(arrowName);
		JPanel contactListPanel = createContactListPanel(contactList);
		namePanel.addMouseListener(new GroupNameMouseListenner(contactListPanel, arrowName));
		this.add(namePanel, BorderLayout.NORTH);
		this.add(contactListPanel, BorderLayout.CENTER);
	}
	
	/**
	 * 创建单个分组中的联系人列表面板
	 * @param contactList 联系人列表
	 * @return 单个分组中的联系人列表面板
	 */
	private JPanel createContactListPanel(List<Contact> contactList) {
		contactListPanel = new JPanel(new GridLayout(contactList.size(), 1, 5, 5));
		contactListPanel.setPreferredSize(new Dimension(290, contactList.size() * 45));
		for (int i = 0; i < contactList.size(); i++) {
			ContactPanel contactPanel = new ContactPanel(contactList.get(i), clientControl);
			contactListPanel.add(contactPanel);
			this.contactPanelMap.put(contactList.get(i).getAccount(), contactPanel);
		}
		contactListPanel.setVisible(false);
		return contactListPanel;
	}
	
	public void addContact(Contact contact) {
		ContactPanel contactPanel = new ContactPanel(contact, clientControl);
		contactListPanel.setLayout(new GridLayout(contactList.size(), 1, 5, 5));
		contactListPanel.setPreferredSize(new Dimension(290, contactList.size() * 45));
		contactListPanel.add(contactPanel);
		contactPanelMap.put(contact.getAccount(), contactPanel);
		countOnline();
		contactListPanel.validate();
	}

	public void unreadRemind(ChatWord chatWord, boolean needCache) {
		if(!hasUnreadRecord) {
			REMINDING = new GroupReminding(arrowName);
			REMINDING.start();
		}
		hasUnreadRecord = true;
		contactPanelMap.get(chatWord.getTalker()).unreadRemind(chatWord, needCache);
	}

	public void clearUnread(Contact contact) {
		contactPanelMap.get(contact.getAccount()).clearUnread();
		this.hasUnreadRecord = false;
		for (Entry<Long, ContactPanel> entry : contactPanelMap.entrySet()) {
			if(entry.getValue().hasUnreadRecord())
				this.hasUnreadRecord = true;
		}
		if(!hasUnreadRecord){
			REMINDING.setExit(true);
			try {
				REMINDING.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.arrowName.setVisible(true);
		}
	}
	
	public void countOnline() {
		int online = 0;
		for (Contact contact : contactList) {
			if(contact.getStatus() != 0)
				online++;
		}
		arrowName.setText(groupName + "（" + online + "/" + contactList.size() + "）");
	}

	public Map<Long, ContactPanel> getContactPanelMap() {
		return contactPanelMap;
	}

	public boolean hasUnreadRecord() {
		return hasUnreadRecord;
	}
	
}

class GroupReminding extends Thread {
	private JLabel name;
	private boolean exit = false;
	
	public GroupReminding(JLabel name){
		this.name = name;
	}
	
	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}
	
	@Override
	public void run() {
		while(!exit){
			try {
				if (name.isVisible()){
					name.setVisible(false);
					Thread.sleep(260);
				} else {
					name.setVisible(true);
					Thread.sleep(400);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
