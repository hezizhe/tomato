package com.lv.qq.client.ui.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.TransparentFrame;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.User;

public class ChatFrame extends TransparentFrame {

	private static final long serialVersionUID = 1L;
	
	private List<ChatTab> tabList = new ArrayList<ChatTab>();
	
	private JPanel chatPanel;
	private JPanel listPanel;
	private JPanel mainPanel;
	//private JScrollPane scroolPanel;
	
	private ClientControl control;
	private User user;
	
	@SuppressWarnings("deprecation")
	public ChatFrame(User user, Contact contact, ClientControl control){
		super(TransparentFrame.FRAME_CLOSE);
		this.control = control;
		this.user = user;
		tabList.add(new ChatTab(this.user, contact, this, control));
		chatPanel = tabList.get(0).getChatContentPanel();
		//设置窗口大小
		this.setSize(970,540);
		//设置窗口居中
		this.setLocationRelativeTo(null);
		//设置是否可见 
		this.setVisible(true);
		this.setContentPane(createMainPanel());
		this.setIconImage(ImgIcon.HEAD_PHOTO.getImage());
		this.show();
	}

	private JPanel createMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		btn_close.setBounds(this.getWidth() - 30, 0, 30, 30);
		btn_minimize.setBounds(this.getWidth() - 60, 0, 30, 30);
		mainPanel.add(btn_close);
		mainPanel.add(btn_minimize);
		mainPanel.add(createScrollPane(), BorderLayout.WEST);
		mainPanel.add(chatPanel, BorderLayout.CENTER);
		return mainPanel;
	}
	
	private JScrollPane createScrollPane(){
		listPanel = new JPanel();
		BoxLayout box = new BoxLayout(listPanel, BoxLayout.Y_AXIS);
		listPanel.setLayout(box);
		listPanel.setBackground(new Color(210, 210, 180));
		listPanel.add(tabList.get(0).getChatTabPanel());
		//fillTabList();
		JScrollPane scroolPanel = new JScrollPane(listPanel);
		scroolPanel.setPreferredSize(new Dimension(220, 540));
		scroolPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scroolPanel;
	}
	
	/*private void fillTabList(){
		listPanel = new JPanel();
		listPanel.setLayout(null);
		listPanel.setBackground(new Color(210, 210, 180));
		for (int i = 0; i < tabList.size(); i++) {
			if(i > 8){
				listPanel.setPreferredSize(new Dimension(220, 60 * (i + 1)));
			}
			JPanel tabPanel = tabList.get(i).getChatTabPanel();
			tabPanel.setBounds(0, 60 * i, 220, 60);
			listPanel.add(tabPanel);
		}
	}*/
	
	public void openNewChat(Contact contact){
		if(!isExistChatAndChoose(contact.getAccount())){
			ChatTab chatTab = new ChatTab(this.user, contact, this, control);
			tabList.add(chatTab);
			listPanel.add(chatTab.getChatTabPanel());
			changeChatTab(chatTab);
		}
		reloadFrame();
	}
	
	protected boolean isExistChatAndChoose(long account){
		boolean exist = false;
		for (ChatTab chatTab : tabList) {
			if(chatTab.getAccount() == account){
				changeChatTab(chatTab);
				exist = true;
			}else{
				chatTab.setTabNotChoose();
			}
		}
		return exist;
	}

	public void closeChatTab(long account) {
		ChatTab tab;
		if(tabList.size() == 1){
			this.setVisible(false);
			this.dispose();
			return;
		}
		for (int i = 0; i < tabList.size(); i++) {
			if(tabList.get(i).getAccount() == account){
				tab = tabList.get(i);
				if(tab.isChoose()){
					int index = i == (tabList.size() - 1) ? (i - 1) : (i + 1);
					ChatTab newTab = tabList.get(index);
					changeChatTab(newTab);
				}
				tabList.remove(tab);
				listPanel.remove(tab.getChatTabPanel());
				reloadFrame();
			}
		}
	}
	
	private void changeChatTab(ChatTab chatTab){
		chatTab.setTabChoosed();
		mainPanel.remove(chatPanel);
		chatPanel = chatTab.getChatContentPanel();
		mainPanel.add(chatPanel, BorderLayout.CENTER);
	}
	
	public void reloadFrame(){
		this.validate();
		listPanel.repaint();
		mainPanel.repaint();
	}

	public void updateFont() {
		for (ChatTab chatTab : tabList) {
			chatTab.setFont();
		}
	}

	/**
	 * 
	 * @param chatWord
	 * @return 0-不存在消息来源的对话框  1-存在但未被选中或窗口被最小化  2-存在并且消息已被展示
	 */
	public int reminding(ChatWord chatWord) {
		for (int i = 0; i < tabList.size(); i++) {
			if(tabList.get(i).getAccount() == chatWord.getTalker()){
				ChatTab tab = tabList.get(i);
				tab.acceptWord(chatWord);
				if(tab.isChoose() && this.getExtendedState() != ICONIFIED){
					return 2;
				}
				tab.remindUnread();
				return 1;
			}
		}
		return 0;
	}

	public void clearUnread(Contact contact) {
		for (ChatTab tab : tabList) {
			if(tab.getAccount() == contact.getAccount()){
				tab.clearUnread();
			}
		}
	}

	public void onlineNotice(long account, boolean online, int status) {
		for (ChatTab tab : tabList) {
			if(tab.getAccount() == account){
				tab.onlineNotice(online, status);
			}
		}
	}

	public boolean cancerTrans(ChatWord chatWord, long breaker, long progress) {
		for (ChatTab tab : tabList) {
			if(tab.getAccount() == chatWord.getTalker()){
				System.out.println(tab.getAccount() + "cancerTrans");
				tab.cancerTrans(chatWord, breaker, progress);
				return true;
			}
		}
		return false;
	}
	
}
