package com.lv.qq.client.ui.chat;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.control.ClientControl;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.ui.client.ExpressionFrame;
import com.lv.qq.client.util.DateTools;
import com.lv.qq.client.util.FileSizeFormatTool;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.User;

public class ChatTab {

	protected Contact contact;
	protected long account;
	protected User user;
	
	private JPanel chatTabPanel;
	private JPanel chatContentPanel;
	
	private JLabel head;
	
	private JPanel chatRecord = new JPanel();
	private JPanel filePanel = new JPanel();
	private JLabel show = new JLabel();
	private JTextArea chatEdit = new JTextArea();
	private JScrollPane scroolPanel;
	private JScrollPane fileScroolPanel;
	
	private Choice fontNameChoice = new Choice();
	private Choice fontStyleChoice = new Choice();
	private Choice fontSizeChoice = new Choice();
	
	private ExpressionFrame expFrame = new ExpressionFrame(chatEdit);
	
	private boolean isChoose = true;
	private Date lastTalkTime;
	private int unreadRecords = 0;
	
	private JLabel close = new JLabel();
	
	private ChatFrame chatFrame;
	private ClientControl control;
	
	private Map<String, FileLoadPanel> loadPanelMap = new HashMap<String, FileLoadPanel>();
	
	public ChatTab(User user, Contact contact, ChatFrame chatFrame, ClientControl control){
		this.control = control;
		this.user = user;
		this.contact = contact;
		this.account = contact.getAccount();
		this.chatFrame = chatFrame;
		createChatTabPanel();
		createChatContentPanel();
	}

	public void createChatTabPanel(){
		chatTabPanel = new JPanel();
		if(isChoose)
			chatTabPanel.setBackground(new Color(181, 181, 162));
		else
			chatTabPanel.setBackground(null);
		chatTabPanel.setPreferredSize(new Dimension(204, 60));
		chatTabPanel.setLayout(null);
		if(contact.getStatus() == 0){
			head = new JLabel(ImgIcon.HEAD_PHOTO_OUTLINE_MINI);
		}else{
			head = new JLabel(ImgIcon.HEAD_PHOTO_MINI);
		}
		head.setBounds(0, 0, 60, 60);
		chatTabPanel.add(head);
		JLabel name = new JLabel(this.contact.getRemarks() + "(" + account + ")");
		name.setBounds(60, 0, 120, 60);
		chatTabPanel.add(name);
		close.setBounds(180, 10, 40, 40);
		close.setHorizontalAlignment(JLabel.CENTER);
		close.setHorizontalTextPosition(JLabel.CENTER); 
		close.setForeground(Color.WHITE);
		initLableClose();
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(!isChoose)
					chatTabPanel.setBackground(null);
				initLableClose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!isChoose)
					chatTabPanel.setBackground(new Color(189, 189, 169));
				close.setVisible(true);
				close.setIcon(ImgIcon.BTN_CLOSE_RED);
				close.setText("");
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				chatFrame.closeChatTab(account);
			}
		});
		chatTabPanel.add(close);
		chatTabPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if(!isChoose){
					chatTabPanel.setBackground(null);
				}
				if(unreadRecords == 0)
					close.setVisible(false);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!isChoose){
					chatTabPanel.setBackground(new Color(189, 189, 169));
				}
				close.setVisible(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(unreadRecords > 0)
					control.clearUnread(contact);
				if(isChoose){
					return;
				}
				chatFrame.isExistChatAndChoose(account);
				chatFrame.reloadFrame();
			}
		});
	}
	
	
	public void createChatContentPanel(){
		chatContentPanel = new JPanel();
		chatContentPanel.setLayout(new BorderLayout());
		chatContentPanel.setBackground(null);
		chatContentPanel.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 204)));
		chatContentPanel.add(crateTitlePanel(), BorderLayout.NORTH);
		chatContentPanel.add(crateTalkPanel(), BorderLayout.CENTER);
		chatContentPanel.add(crateFilePanel(), BorderLayout.EAST);
		chatContentPanel.setBounds(0, 0, 750, 530);
	}
	
	private JPanel crateTitlePanel() {
		//JPanel titlePanel = new JPanel();
		JPanel titlePanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.CHAT_TITLE_BG.getImage(), 0, 0, 750, 85, null);
			}
		};
		titlePanel.setLayout(null);
		JLabel name = new JLabel(this.contact.getRemarks());
		name.setFont(new Font("黑体", Font.BOLD, 28));
		name.setBounds(10, 10, 300, 65);
		titlePanel.add(name);
		titlePanel.setPreferredSize(new Dimension(750, 85));
		return titlePanel;
	}
	
	private JPanel crateTalkPanel() {
		JPanel talkPanel = new JPanel(new BorderLayout());
		talkPanel.setPreferredSize(new Dimension(520, 455));
		chatRecord.setBackground(Color.WHITE);
		BoxLayout box = new BoxLayout(chatRecord, BoxLayout.Y_AXIS);
		chatRecord.setLayout(box);
		scroolPanel = new JScrollPane(chatRecord);
		scroolPanel.setPreferredSize(new Dimension(520, 335));
		scroolPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		talkPanel.add(scroolPanel, BorderLayout.NORTH);
		
		JPanel editPanel = new JPanel(new BorderLayout());
		chatEdit.setPreferredSize(new Dimension(520, 53));
		chatEdit.setLineWrap(true);
		chatEdit.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 204)));
		chatEdit.setFont(new Font(PersonalConfig.FONT_NAME, PersonalConfig.FONT_STYLE, PersonalConfig.FONT_SIZE));
		//添加焦点事件（当鼠标点击时消除当前对话框的消息提醒）
		chatEdit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				controlCleanUnread();
				expFrame.setVisible(false);
			}
		});
		editPanel.add(chatEdit, BorderLayout.NORTH);
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnClose = new JButton(ImgIcon.BTN_ICON_CLOSE);
		btnClose.setPreferredSize(new Dimension(70, 24));
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chatFrame.closeChatTab(account);
			}
		});
		btnPanel.add(btnClose);
		JButton btnSend = new JButton(ImgIcon.BTN_ICON_SEND);
		btnSend.setPreferredSize(new Dimension(70, 24));
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word = chatEdit.getText();
				if(!word.equals("")){
					chatEdit.setText("");
					Font font = chatEdit.getFont();
					ChatWord chatWord = new ChatWord(word, user.getAccount(), contact.getAccount(), font);
					addWordPanel(chatWord, WordPanel.RIGHT, null);
					control.sendWord(chatWord);
				}
				chatEdit.requestFocus();
			}
		});
		btnPanel.add(btnSend);
		editPanel.add(btnPanel, BorderLayout.CENTER);
		talkPanel.add(editPanel, BorderLayout.SOUTH);
		
		JPanel fontPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fontPanel.add(new JLabel("字体："));
		for (int i = 0; i < PersonalConfig.FONT_HOME.length; i++) {
			fontNameChoice.add(PersonalConfig.FONT_HOME[i]);
		}
		fontNameChoice.select(PersonalConfig.FONT_NAME);
		fontNameChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String name = (String) e.getItem();
				PersonalConfig.FONT_NAME = name;
				chatFrame.updateFont();
			}
		});
		fontPanel.add(fontNameChoice);
		fontPanel.add(new JLabel("样式："));
		for (String style : PersonalConfig.getStyleSet()) {
			fontStyleChoice.add(style);
		}
		fontStyleChoice.select(PersonalConfig.getFontStyle());
		fontStyleChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String style = (String) e.getItem();
				PersonalConfig.setFontStyle(style);
				chatFrame.updateFont();
			}
		});
		fontPanel.add(fontStyleChoice);
		fontPanel.add(new JLabel("字号："));
		for (int i = 13; i < 31; i++) {
			fontSizeChoice.add(String.valueOf(i));
		}
		fontSizeChoice.select(String.valueOf(PersonalConfig.FONT_SIZE));
		fontSizeChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String size = (String) e.getItem();
				PersonalConfig.FONT_SIZE = Integer.parseInt(size);
				chatFrame.updateFont();
			}
		});
		fontPanel.add(fontSizeChoice);
		JLabel expression = new JLabel(ImgIcon.EXPRESSION);
		expression.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				expFrame.setLocation(chatFrame.getX() + 493, chatFrame.getY() + 300);
				expFrame.setVisible(true);
			}
		});
		fontPanel.add(expression);
		fontPanel.setPreferredSize(new Dimension(520, 30));
		talkPanel.add(fontPanel, BorderLayout.CENTER);
		
		return talkPanel;
	}
	
	private JScrollPane crateFilePanel() {
		BoxLayout box = new BoxLayout(filePanel, BoxLayout.Y_AXIS);
		filePanel.setLayout(box);
		filePanel.setTransferHandler(new TransferHandler(){
			private static final long serialVersionUID = 1L;
			@Override
		    public boolean canImport(TransferHandler.TransferSupport support) {
		        for (DataFlavor flavor : support.getDataFlavors()) {
		            if (flavor.isFlavorJavaFileListType()) {
		                return true;
		            }
		        }
		        return false;
		    }
		    @Override
		    @SuppressWarnings("unchecked")
		    public boolean importData(TransferHandler.TransferSupport support) {
		        if (!this.canImport(support))
		            return false;
		        List<File> files;
		        try {
		            files = (List<File>) support.getTransferable()
		                    .getTransferData(DataFlavor.javaFileListFlavor);
		        } catch (UnsupportedFlavorException | IOException ex) {
		            // 不应该出现 (除非JDK异常)
		            return false;
		        }
		        for (File file: files) {
		            // TODO 上传文件
		        	System.out.println(file.getAbsolutePath());
		        	ChatWord chatWord = new ChatWord(user.getAccount(), contact.getAccount(), file);
		        	FileLoadPanel flp = addFileLoadPanel(chatWord, 1);
		        	control.uploadFile(chatWord, file, flp);
		        	controlCleanUnread();
		        }
		        return true;
		    }
		});
		if(contact.getGender() == 1){
			show.setIcon(ImgIcon.SHOW_MAN);
		}else if(contact.getGender() == 2){
			show.setIcon(ImgIcon.SHOW_WOMAN);
		}else if(contact.getGender() == 0){
			show.setIcon(ImgIcon.SHOW_SECRET);
		}else if(contact.getGender() == 3){
			show.setIcon(ImgIcon.SHOW_LES);
		}else if(contact.getGender() == 4){
			show.setIcon(ImgIcon.SHOW_GAY);
		}
		filePanel.add(show);
		fileScroolPanel = new JScrollPane(filePanel);
		fileScroolPanel.setPreferredSize(new Dimension(230, 455));
		fileScroolPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return fileScroolPanel;
	}
	
	public void setFont(){
		this.chatEdit.setFont(new Font(PersonalConfig.FONT_NAME, PersonalConfig.FONT_STYLE, PersonalConfig.FONT_SIZE));
		fontSizeChoice.select(String.valueOf(PersonalConfig.FONT_SIZE));
		fontNameChoice.select(PersonalConfig.FONT_NAME);
		fontStyleChoice.select(PersonalConfig.getFontStyle());
	}

	public void acceptWord(ChatWord chatWord) {
		if(chatWord.getType() == 1){
			addWordPanel(chatWord, WordPanel.LEFT, null);
			scroolPanel.validate();
		}else if (chatWord.getType() == 2) {
			addFileLoadPanel(chatWord, 2);
		}
	}
	
	private void addWordPanel(ChatWord chatWord, int layout, String name){
		String showTime = null;
		if(lastTalkTime == null){
			lastTalkTime = chatWord.getTime();
		}
		long interval = chatWord.getTime().getTime() - lastTalkTime.getTime();
		if(interval > 10000 || interval == 0){
			showTime = DateTools.formatTime(chatWord.getTime(), "HH:mm:ss");
		}
		if(chatWord.getType() == 1){
			chatRecord.add(new WordPanel(chatWord.getWord(), chatWord.getFont(), layout, showTime, name));
		}else if(chatWord.getType() == 2) {
			chatRecord.add(new WordPanel(chatWord, layout, showTime, name));
		}
		chatRecord.validate();
		changeScrollBar();
	}
	
	private FileLoadPanel addFileLoadPanel(ChatWord chatWord, int layout){
		show.setVisible(false);
		FileLoadPanel flp = new FileLoadPanel(layout, chatWord, this.control, this);
		this.filePanel.add(flp);
		loadPanelMap.put(chatWord.getId(), flp);
		this.fileScroolPanel.validate();
		return flp;
	}
	
	public void transSuccess(ChatWord chatWord) {
		removeLoadPanel(chatWord.getId());
		//显示传输完成的文件
		if(chatWord.getTalker() == user.getAccount()){
			addWordPanel(chatWord, WordPanel.RIGHT, null);
		}else if (chatWord.getListener() == user.getAccount()) {
			addWordPanel(chatWord, WordPanel.LEFT, contact.getRemarks());
		}
		changeScrollBar();
	}
	
	public void cancerTrans(ChatWord chatWord, long breaker, long progress) {
		removeLoadPanel(chatWord.getId());
		String msgText;
		if (user.getAccount() == breaker){
			if (chatWord.getListener() == user.getAccount()){
				if (progress > 0) {
					msgText = "您终止了“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")的接受，该文件已经接收" + FileSizeFormatTool.formatFileSize(progress) + "。";
				} else {
					msgText = "您取消接收“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")，文件传输失败。";
				}
			} else {
				if (progress > 0) {
					msgText = "您终止了“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")的发送，该文件已经发送" + FileSizeFormatTool.formatFileSize(progress) + "。";
				} else {
					msgText = "您取消了“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")的发送，文件传输失败。";
				}
			}
		} else {
			if (chatWord.getTalker() == user.getAccount()){
				if (progress > 0) {
					msgText = "对方中断了“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")的接受，该文件已经发送" + FileSizeFormatTool.formatFileSize(progress) + "。";
				} else {
					msgText = "对方取消接收“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")，文件发送失败。";
				}
			} else {
				if (progress > 0) {
					msgText = "对方中断了“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")的发送，该文件已经接收" + FileSizeFormatTool.formatFileSize(progress) + "。";
				} else {
					msgText = "对方取消了“" + chatWord.getFileName() +"”(" + FileSizeFormatTool.formatFileSize(chatWord.getFileLength())
								+ ")的发送。";
				}
			}
		}
		JPanel wrongMsg = new JPanel(){
			private static final long serialVersionUID = 1L;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.WRONG_MSG_BG.getImage(), 86, 0, 428, 46, 0, 0, 342, 46, null);
			}
		};
		wrongMsg.setLayout(null);
		JTextArea text = new JTextArea();
		text.setText(msgText);
		text.setFont(new Font("黑体", Font.PLAIN, 12));
		text.setLineWrap(true);
		text.setEditable(false);
		text.setBounds(117, 3, 292, 40);
		text.setForeground(new Color(119, 119, 119));
		text.setBackground(new Color(219, 220, 219));
		wrongMsg.add(text);
		wrongMsg.setBackground(Color.WHITE);
		wrongMsg.setPreferredSize(new Dimension(514, 46));
		chatRecord.add(wrongMsg);
		chatRecord.validate();
		changeScrollBar();
	}
	
	private void removeLoadPanel(String id){
		this.filePanel.remove(this.loadPanelMap.get(id));
		this.loadPanelMap.remove(id);
		if(loadPanelMap.size() == 0){
			show.setVisible(true);
		}
		this.fileScroolPanel.validate();
	}
	
	private void changeScrollBar(){
		JScrollBar bar = scroolPanel.getVerticalScrollBar();
		bar.setValue(bar.getMaximum() + 10);
	}
	
	public void remindUnread(){
		unreadRecords++;
		initLableClose();
	}
	
	public void clearUnread() {
		unreadRecords = 0;
		initLableClose();
	}
	
	public void controlCleanUnread(){
		if(unreadRecords > 0)
			control.clearUnread(contact);
	}
	
	private void initLableClose(){
		if(unreadRecords > 0){
			if(unreadRecords < 10){
				close.setIcon(ImgIcon.DOT_RED_S);
				close.setText(String.valueOf(unreadRecords));
			} else if(unreadRecords < 100){
				close.setIcon(ImgIcon.DOT_RED_M);
				close.setText(String.valueOf(unreadRecords));
			} else {
				close.setIcon(ImgIcon.DOT_RED_L);
				close.setText(String.valueOf("99+"));
			}
			close.setVisible(true);
		}else {
			close.setIcon(ImgIcon.BTN_CLOSE_GRAY);
			close.setText("");
			close.setVisible(false);
		}
	}
	
	public void setTabChoosed() {
		this.isChoose = true;
		this.chatTabPanel.setBackground(new Color(181, 181, 162));
	}
	
	public void setTabNotChoose() {
		this.isChoose = false;
		this.chatTabPanel.setBackground(null);
	}
	
	public void onlineNotice(boolean online, int status) {
		if(online){
			head.setIcon(ImgIcon.HEAD_PHOTO_MINI);
		}else {
			head.setIcon(ImgIcon.HEAD_PHOTO_OUTLINE_MINI);
		}
	}
	
	public boolean isChoose() {
		return isChoose;
	}

	public long getAccount() {
		return account;
	}

	public JPanel getChatTabPanel() {
		return chatTabPanel;
	}

	public JPanel getChatContentPanel() {
		return chatContentPanel;
	}
	
}