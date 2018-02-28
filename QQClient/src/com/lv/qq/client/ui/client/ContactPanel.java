package com.lv.qq.client.ui.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;

public class ContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private boolean hasUnreadRecord = false;
	
	private JLabel head;
	
	private ContactReminding REMINDING;
	
	private Contact contact;
	private ClientControl clientControl;
	
	private List<ChatWord> chatCache = new ArrayList<ChatWord>();
	
	/**
	 * 创建单个联系人面板
	 * @param contact 联系人信息
	 * @return 单个联系人面板
	 */
	public ContactPanel(Contact cont, ClientControl control) {
		this.contact = cont;
		this.clientControl = control;
		this.setLayout(null);
		Font font = new Font("宋体", Font.PLAIN, 12);
		this.setFont(font);
		if(contact.getStatus() == 0){
			head = new JLabel(ImgIcon.HEAD_PHOTO_OUTLINE_MINI);
		}else{
			head = new JLabel(ImgIcon.HEAD_PHOTO_MINI);
		}
		head.setBounds(10, 0, 40, 40);
		JLabel remarks = new JLabel(contact.getRemarks() + "（" + contact.getUserName() + "）");
		remarks.setFont(font);
		remarks.setBounds(60, 0, 230, 20);
		JLabel signature = new JLabel(contact.getSignature());
		signature.setFont(font);
		signature.setBounds(60, 20, 230, 20);
		this.add(head);
		this.add(remarks);
		this.add(signature);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				((JPanel)e.getSource()).setBackground(null);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				((JPanel)e.getSource()).setBackground(new Color(202, 202, 202, 255));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					clientControl.openChatFrame(contact, chatCache);
					if(hasUnreadRecord)
						clientControl.clearUnread(contact);
				}
			}
		});
	}

	public void unreadRemind(ChatWord chatWord, boolean needCache) {
		if(!hasUnreadRecord) {
			REMINDING = new ContactReminding(head);
			REMINDING.start();
		}
		this.hasUnreadRecord = true;
		if(needCache){
			chatCache.add(chatWord);
		}
	}
	
	public void clearUnread() {
		REMINDING.setExit(true);
		try {
			REMINDING.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hasUnreadRecord = false;
		head.setVisible(true);
		chatCache.clear();
	}

	public boolean hasUnreadRecord() {
		return hasUnreadRecord;
	}

	public void onlineNotice(boolean online, int status) {
		if(online){
			head.setIcon(ImgIcon.HEAD_PHOTO_MINI);
		}else{
			head.setIcon(ImgIcon.HEAD_PHOTO_OUTLINE_MINI);
		}
	}

	public void removeChatWord(ChatWord chatWord) {
		// TODO Auto-generated method stub chatCache
		for (int i = 0; i < chatCache.size(); i++) {
			if(chatCache.get(i).getId().equals(chatWord.getId())){
				chatCache.remove(i);
			}
		}
		if(chatCache.size() == 0){
			clientControl.clearUnread(contact);
		}
	}

}

class ContactReminding extends Thread {
	private JLabel head;
	private boolean exit = false;
	
	public ContactReminding(JLabel head){
		this.head = head;
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
				if (head.isVisible()){
					head.setVisible(false);
					Thread.sleep(260);
				} else {
					head.setVisible(true);
					Thread.sleep(400);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
