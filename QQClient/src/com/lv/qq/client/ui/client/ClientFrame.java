package com.lv.qq.client.ui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.SimpleButton;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.User;

public class ClientFrame extends TransparentFrame {
	private static final long serialVersionUID = 1L;
	
	private ClientControl clientControl;
	
	private User user;
	private List<ContactGroup> contactGroups;
	private Map<Integer, ContactGroupPanel> groupPanelMap = new HashMap<Integer, ContactGroupPanel>();
	
	private JTextField nameEdit;
	private JTextField signatureEdit;
	private JLabel name;
	private JLabel signature;
	private JPanel listPanel = new JPanel();
	
	private boolean hasUnreadRecord = false;
	private ClientReminding REMINDING;
	
	@SuppressWarnings("deprecation")
	public ClientFrame(User user, List<ContactGroup> contactGroups, ClientControl clientControl){
		super(TransparentFrame.SYSTEM_EXIT);
		this.user = user;
		this.contactGroups = contactGroups;
		this.clientControl = clientControl;
		//设置窗口大小
		this.setSize(290,660);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//设置是否可见 
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
	}

	/**
	 * 创建主面板，包含主页信息和联系人列表
	 * @return
	 */
	private Container createMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel bgLabel = new JLabel(ImgIcon.BG_CLIENT);
		bgLabel.setBounds(0, 0, 290, 150);
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_minimize.setBounds(this.getWidth() - 60, 0, 30, 30);
		mainPanel.add(btn_close);
		mainPanel.add(btn_minimize);
		JLabel head = new JLabel(headPhoto);
		head.setBounds(13, 46, 80, 80);
		mainPanel.add(head);
		nameEdit = new JTextField(user.getUserName());
		nameEdit.setFont(new Font("黑体", Font.BOLD, 18));
		nameEdit.setBounds(100, 46, 180, 28);
		nameEdit.setVisible(false);
		nameEdit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				nameEdit.setVisible(false);
				name.setVisible(true);
				String newName = nameEdit.getText();
				if("".equals(newName.trim())){
					nameEdit.setText(user.getUserName());
				}else{
					user.setUserName(newName);
					clientControl.updateUser(user);
					nameEdit.setText(newName);
					name.setText(newName);
				}
			}
		});
		nameEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					nameEdit.setVisible(false);
					name.setVisible(true);
					name.requestFocus();
				}
			}
		});
		mainPanel.add(nameEdit);
		name = new JLabel(user.getUserName());
		name.setFont(new Font("黑体", Font.BOLD, 18));
		name.setBounds(100, 46, 180, 28);
		name.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					name.setVisible(false);
					nameEdit.setVisible(true);
				}
			}
		});
		mainPanel.add(name);
		signatureEdit = new JTextField(user.getSignature());
		signatureEdit.setFont(new Font("宋体", Font.PLAIN, 12));
		signatureEdit.setBounds(100, 74, 180, 20);
		signatureEdit.setVisible(false);
		signatureEdit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				signatureEdit.setVisible(false);
				signature.setVisible(true);
				String newSignature = signatureEdit.getText();
				user.setSignature(newSignature);
				clientControl.updateUser(user);
				signatureEdit.setText(newSignature);
				signature.setText(newSignature);
			}
		});
		signatureEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					signatureEdit.setVisible(false);
					signature.setVisible(true);
					signature.requestFocus();
				}
			}
		});
		mainPanel.add(signatureEdit);
		signature = new JLabel(user.getSignature());
		signature.setFont(new Font("宋体", Font.PLAIN, 12));
		signature.setBounds(100, 74, 180, 20);
		signature.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					signature.setVisible(false);
					signatureEdit.setVisible(true);
				}
			}
		});
		mainPanel.add(signature);
		mainPanel.add(bgLabel, BorderLayout.NORTH);
		mainPanel.add(createListPanel(), BorderLayout.CENTER);
		mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
		return mainPanel;
	}
	
	/**
	 * 创建联系人滚动面板，包含联系人分组
	 * @return 联系人滚动面板
	 */
	private JScrollPane createListPanel() {
		BoxLayout box = new BoxLayout(listPanel, BoxLayout.Y_AXIS);
		listPanel.setLayout(box);
		fillContactList();
		JScrollPane scroolPanel = new JScrollPane(listPanel);
		scroolPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scroolPanel;
	}
	
	private void fillContactList(){
		if(contactGroups.size() == 0){
			JPanel tipPanel = new JPanel(null);
			tipPanel.setPreferredSize(new Dimension(290, 300));
			JLabel tip = new JLabel("<html><body>您还没有好友,<br>请点击左下角加号添加好友！</body></html>");
			tip.setBounds(50, 200, 200, 50);
			tipPanel.add(tip);
			listPanel.add(tipPanel);
		}
		for (ContactGroup group : contactGroups) {
			ContactGroupPanel contGroup = new ContactGroupPanel(group.getGoupName(), group.getContactList(), this.clientControl);
			listPanel.add(contGroup);
			this.groupPanelMap.put(group.getGroupId(), contGroup);
		}
	}
	
	private JPanel createBottomPanel() {
		JPanel buttomPanel = new JPanel(null);
		buttomPanel.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 204)));
		buttomPanel.setPreferredSize(new Dimension(290, 40));
		SimpleButton btnAdd = new SimpleButton(ImgIcon.BTN_ADD);
		SimpleButton btnMenu = new SimpleButton(ImgIcon.BTN_MENU);
		btnMenu.setBounds(10, 0, 20, 40);
		btnAdd.setBounds(40, 0, 20, 40);
		btnMenu.setColorEnter(new Color(228, 228, 226));
		btnAdd.setColorEnter(new Color(228, 228, 226));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientControl.openAddUserFrame();
			}
		});
		buttomPanel.add(btnMenu);
		buttomPanel.add(btnAdd);
		buttomPanel.setBackground(Color.WHITE);
		return buttomPanel;
	}
	
	public void unreadRemind(ChatWord chatWord, boolean needCache) {
		Contact contact = null;
		for (ContactGroup group : contactGroups) {
			List<Contact> contactList = group.getContactList();
			for (Contact con : contactList) {
				if(con.getAccount() == chatWord.getTalker()){
					contact = con;
				}
			}
		}
		if (contact == null){
			//TODO 陌生人
		} else {
			if(!hasUnreadRecord){
				REMINDING = new ClientReminding(this);
				REMINDING.start();
			}
			hasUnreadRecord = true;
			groupPanelMap.get(contact.getGroupId()).unreadRemind(chatWord, needCache);
		}
		
	}

	public void clearUnread(Contact contact) {
		groupPanelMap.get(contact.getGroupId()).clearUnread(contact);
		this.hasUnreadRecord = false;
		for (Entry<Integer, ContactGroupPanel> entry : groupPanelMap.entrySet()) {
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
			this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		}
	}

	public void onlineNotice(long account, boolean online, int status) {
		int groupId = 999;
		Contact contact = null;
		for (ContactGroup group : contactGroups) {
			List<Contact> contactList = group.getContactList();
			int gid = group.getGroupId();
			for (Contact con : contactList) {
				if(con.getAccount() == account){
					groupId = gid;
					con.setStatus(status);
					contact = con;
				}
			}
		}
		if (groupId == 999) {
			//对方非用户好友，连陌生人都不是，此种情况应在程序中避免
		} else {
			groupPanelMap.get(groupId).getContactPanelMap().get(account).onlineNotice(online, status);
			groupPanelMap.get(groupId).countOnline();
			//TODO
			new OnlinNoticeFrame(contact);
		}
	}

	public void removeChatWord(ChatWord chatWord) {
		int groupId = 999;
		for (ContactGroup group : contactGroups) {
			List<Contact> contactList = group.getContactList();
			int gid = group.getGroupId();
			for (Contact con : contactList) {
				if(con.getAccount() == chatWord.getTalker()){
					groupId = gid;
				}
			}
		}
		groupPanelMap.get(groupId).getContactPanelMap().get(chatWord.getTalker()).removeChatWord(chatWord);
	}

	public void updateUser(User user) {
		this.user = user;
		this.name.setText(user.getUserName());
		this.nameEdit.setText(user.getUserName());
		this.signature.setText(user.getSignature());
		this.signatureEdit.setText(user.getSignature());
	}

	public void addContact(Contact contact) {
		this.groupPanelMap.get(contact.getGroupId()).addContact(contact);
	}

	public void addContactGroup(ContactGroup contactGroup) {
		if(contactGroups.size() == 0){
			listPanel.removeAll();
		}
		ContactGroupPanel contGroup = new ContactGroupPanel(contactGroup.getGoupName(), contactGroup.getContactList(), this.clientControl);
		listPanel.add(contGroup);
		this.groupPanelMap.put(contactGroup.getGroupId(), contGroup);
		listPanel.validate();
	}
	
}

class ClientReminding extends Thread {
	private JFrame frame;
	private boolean exit = false;
	
	public ClientReminding(JFrame frame){
		this.frame = frame;
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
				if (frame.getIconImage() == ImgIcon.HEAD_PHOTO.getImage()){
					frame.setIconImage(ImgIcon.NULL.getImage());
					Thread.sleep(260);
				} else {
					frame.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
					Thread.sleep(400);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
