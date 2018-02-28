package com.lv.qq.client.control;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.ui.Audio;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.ui.WelcomeFrame;
import com.lv.qq.client.ui.adduser.AddConfirmFrame;
import com.lv.qq.client.ui.adduser.AddUserFrame;
import com.lv.qq.client.ui.chat.ChatFrame;
import com.lv.qq.client.ui.chat.FileLoadPanel;
import com.lv.qq.client.ui.client.AddRequestFrame;
import com.lv.qq.client.ui.client.ClientFrame;
import com.lv.qq.client.ui.client.SystemInformFrame;
import com.lv.qq.client.ui.login.LoginFailFrame;
import com.lv.qq.client.ui.login.LoginFrame;
import com.lv.qq.client.ui.register.RegisterFrame;
import com.lv.qq.client.ui.userinfo.UserInfoFrame;
import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.Message;
import com.lv.qq.common.vo.User;

@SuppressWarnings("unchecked")
public class ClientControl {
	
	private static String IP = "172.16.200.17";//"127.0.0.1";
	private static int PORT = 9998;
	
	private static Map<Integer, String> METHOD_MAP;
	
	static{
		METHOD_MAP = new HashMap<Integer, String>();
		METHOD_MAP.put(3, "onlineNotice");
		METHOD_MAP.put(4, "outlineNotice");
		METHOD_MAP.put(5, "updateUserResult");
		METHOD_MAP.put(8, "acceptUserList");
		METHOD_MAP.put(9, "sendResult");
		METHOD_MAP.put(10, "newChatWord");
		METHOD_MAP.put(14, "cancerTrans");
		METHOD_MAP.put(16, "refreshContacts");
	}
	
	private WelcomeFrame welcomeFrame;
	private LoginFrame loginFrame;
	private ChatFrame chatFrame;
	private LoginFailFrame loginFailFrame;
	private ClientFrame clientFrame;
	private RegisterFrame registerFrame;
	private UserInfoFrame userInfoFrame;
	private AddUserFrame addUserFrame;
	
	private CommunicationThread thread;
	
	private Map<String, LoadThread> loadThreadMap = new HashMap<String, LoadThread>();
	private Map<String, FileLoadPanel> loadPanelMap = new HashMap<String, FileLoadPanel>();
	
	private List<ContactGroup> contactGroups;
	
	private User user;
	
	public ClientControl(){
		welcomeFrame = new WelcomeFrame();
		ImgIcon.BG_LOGIN.getImage();
		Audio.MSG_RING.getClass();
		PersonalConfig.init(this);
		loginFrame = new LoginFrame(this);
		welcomeFrame.setVisible(false);
		loginFrame.setVisible(true);
	}
	
	public void login(Long account, String password, boolean remenberPwd, boolean autoLogin){
		System.out.println(account + "-" + password + "-" +  remenberPwd + "-" +  autoLogin);
		User loginUser = new User();
		loginUser.setAccount(account);
		loginUser.setPassword(password);
		Message loginRequest = new Message(0, loginUser);
		List<Object> list = requestServer(loginRequest);
		Message response = (Message) list.get(0);
		Map<String, Object> result = (Map<String, Object>) response.getContent();
		System.out.println("result：" + result);
		if((int)result.get("result") == 1){
			this.user = (User)result.get("user");
			loginFrame.setVisible(false);
			thread = new CommunicationThread((Socket) list.get(1), this);
			thread.start();
			contactGroups = (List<ContactGroup>)result.get("contactGroups");
			clientFrame = new ClientFrame(this.user, contactGroups, this);
			List<ChatWord> unreadMessages = (List<ChatWord>) result.get("unreadMessages");
			for (ChatWord chatWord : unreadMessages) {
				newChatWord(new Message(10, chatWord));
			}
		}else{
			loginFailFrame = new LoginFailFrame(this);
			loginFrame.cancer();
			loginFrame.setVisible(false);
		}
	}
	
	private List<Object> requestServer(Message request){
		List<Object> list = new ArrayList<Object>();
		Message response = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Socket socket = null;
		try {
			socket = new Socket(IP, PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(request);
			ois = new ObjectInputStream(socket.getInputStream());
			response = (Message) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		list.add(response);
		list.add(socket);
		return list;
	}
	
	public void openChatFrame(Contact contact, List<ChatWord> chatCache){
		if(chatFrame == null || !chatFrame.isVisible()) {
			chatFrame = new ChatFrame(user, contact, this);
		} else {
			chatFrame.openNewChat(contact);
		}
		if(chatFrame.getExtendedState() == Frame.ICONIFIED){
			chatFrame.setExtendedState(Frame.NORMAL); 
		}
		chatFrame.requestFocus();
		if(chatCache.size() > 0){
			for (int i = 0; i < chatCache.size(); i++) {
				chatFrame.reminding(chatCache.get(i));
			} 
		}
	}

	public void backLogin() {
		loginFrame.setVisible(true);
		loginFailFrame.setVisible(false);
	}

	public void sendWord(ChatWord chatWord) {
		Message message = new Message(9, chatWord);
		thread.sendMessage(message);
	}
	
	public void dealWith(Message message){
		String methodName = METHOD_MAP.get(message.getType());
		try {
			System.out.println("执行的方法:" + methodName);
			Method method = this.getClass().getMethod(methodName, Message.class);
			method.invoke(this, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendResult(Message message){
		System.out.println("消息发送成功！");
	}
	
	public void newChatWord(Message message){
		ChatWord chatWord = (ChatWord) message.getContent();
		if(chatWord.getType() == 3){ //添加好友请求
			new AddRequestFrame(this, chatWord);
			Audio.SYSTEM.play();
			return;
		}else if(chatWord.getType() == 4){ //系统消息
			new SystemInformFrame(chatWord.getWord());
			hadReadOne(chatWord);
			Audio.SYSTEM.play();
			return;
		}
		Audio.MSG_RING.play();
		//当对话窗口未打开或者已关闭，把数据暂存到主窗口中的消息来源处并提示
		if(chatFrame == null || !chatFrame.isVisible()){
			clientFrame.unreadRemind(chatWord, true);
		} else {
			int state = chatFrame.reminding(chatWord);
			if(state == 2){  //对话窗口已处理并展示了消息，返回已读状态，主窗口不需要任何操作
				hadRead(chatWord.getTalker());
			}else if (state == 1) {  //对话窗口已处理但未展示消息，主窗口需要提示消息
				clientFrame.unreadRemind(chatWord, false);
			}else {  //对话窗口没有消息来源的对话框，主窗口需要提示并暂时存储消息
				clientFrame.unreadRemind(chatWord, true);
			}
		};
	}

	public void clearUnread(Contact contact) {
		clientFrame.clearUnread(contact);
		if(chatFrame != null){
			chatFrame.clearUnread(contact);
		}
		hadRead(contact.getAccount());
	}
	
	public void checkAddRequest(ChatWord chatWord) {
		User requstUser = getUser(chatWord.getTalker());
		new AddConfirmFrame(this, user, requstUser, contactGroups, chatWord);
	}
	
	private User getUser(long account){
		Message message = new Message(6, account);
		List<Object> reply = requestServer(message);
		Message response = (Message) reply.get(0);
		Socket socket = (Socket) reply.get(1);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (User)response.getContent();
	}
	
	public void hadRead(long account){
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("talker", account);
		map.put("listener", user.getAccount());
		Message message = new Message(10, map);
		thread.sendMessage(message);
	}
	
	public void hadReadOne(ChatWord chatWord){
		Message message = new Message(17, chatWord);
		thread.sendMessage(message);
	}
	
	public void onlineNotice(Message message){
		Audio.KNOCK.play();
		Map<String, Long> statusMap = (Map<String, Long>) message.getContent();
		long account = statusMap.get("account");
		int status = statusMap.get("status").intValue();
		clientFrame.onlineNotice(account, true, status);
		if(chatFrame != null){
			chatFrame.onlineNotice(account, true, status);
		}
	}
	
	public void outlineNotice(Message message){
		Audio.KNOCK.play();
		long account = (long) message.getContent();
		clientFrame.onlineNotice(account, false, 0);
		if(chatFrame != null){
			chatFrame.onlineNotice(account, false, 0);
		}
	}

	public void uploadFile(ChatWord chatWord, File file, FileLoadPanel flp) {
		//控制最多同时上传3个文件
		ExecutorService service = Executors.newFixedThreadPool(3);
		FileUploadThread uploadThread = new FileUploadThread(IP, PORT, chatWord, file, flp, this);
		this.loadThreadMap.put(chatWord.getId(), uploadThread);
		this.loadPanelMap.put(chatWord.getId(), flp);
		service.execute(uploadThread);
	}

	public void acceptFile(ChatWord chatWord, FileLoadPanel flp) {
		//控制最多同时接收3个文件
		ExecutorService service = Executors.newFixedThreadPool(3);
		FileDownloadThread downloadThread = new FileDownloadThread(chatWord, IP, PORT, flp, this);
		this.loadThreadMap.put(chatWord.getId(), downloadThread);
		this.loadPanelMap.put(chatWord.getId(), flp);
		service.execute(downloadThread);
	}

	public void transSuccess(String id) {
		this.loadThreadMap.remove(id);
		this.loadPanelMap.get(id).transSuccess();
		this.loadPanelMap.remove(id);
	}

	public void requstCancerTrans(ChatWord chatWord) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chatWord", chatWord);
		map.put("breaker", user.getAccount());
		Message message = new Message(14, map);
		thread.sendMessage(message);
	}
	
	public void cancerTrans(Message message){
		Map<String, Object> map = (Map<String, Object>)message.getContent();
		ChatWord chatWord = (ChatWord) map.get("chatWord");
		long breaker = (long) map.get("breaker");
		LoadThread loadThread = this.loadThreadMap.get(chatWord.getId());
		long progress = 0;
		if (loadThread != null){
			//中止本地的文件传输
			loadThread.cancer();
			if(loadThread instanceof FileDownloadThread) {
				//socket接收文件会造成线程阻塞，暴力结束线程
				loadThread.closeResourse();
			}
			progress = loadThread.getProgress();
			this.loadPanelMap.get(chatWord.getId()).cancerTrans(breaker, progress);
			this.loadThreadMap.remove(chatWord.getId());
			this.loadPanelMap.remove(chatWord.getId());
		} else {
			if (chatFrame == null || !chatFrame.isVisible()) {
				clientFrame.removeChatWord(chatWord);
			} else {
				if(!this.chatFrame.cancerTrans(chatWord, breaker, progress)){
					clientFrame.removeChatWord(chatWord);
				}
			}
		}
	}

	public void updateUser(User newUser) {
		Message message = new Message(3, newUser);
		thread.sendMessage(message);
	}
	
	public void updateUserResult(Message message){
		Map<String, Object> map = (Map<String, Object>) message.getContent();
		User newUser = (User) map.get("user");
		this.user = newUser;
		this.clientFrame.updateUser(user);
	}

	public void register(User user) {
		Message message = new Message(2, user);
		List<Object> reply = requestServer(message);
		Message response = (Message) reply.get(0);
		Socket socket = (Socket) reply.get(1);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		User u = (User) response.getContent();
		if(u.getAccount() != 0){
			registerFrame.dispose();
			registerFrame.setVisible(false);
			loginFrame.fillAccount(u.getAccount());
			userInfoFrame = new UserInfoFrame(this, u);
		}
	}
	
	public Map<String, List<Dictionary>> getDictionarys() {
		Message message = new Message(4, user);
		List<Object> reply = requestServer(message);
		Message response = (Message) reply.get(0);
		Socket socket = (Socket) reply.get(1);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, List<Dictionary>> dictionarys = (Map<String, List<Dictionary>>) response.getContent();
		return dictionarys;
	}

	public void createRegisterFrame() {
		registerFrame = new RegisterFrame(this);
	}

	public void perfectUserInfo(User user) {
		if(thread != null){
			updateUser(user);
		}else{
			Message message = new Message(3, user);
			List<Object> reply = requestServer(message);
			Message response = (Message) reply.get(0);
			Socket socket = (Socket) reply.get(1);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Map<String, Object> map = (Map<String, Object>) response.getContent();
			boolean updateSuccess = (boolean) map.get("result");
			this.userInfoFrame.showResult(updateSuccess);
			this.userInfoFrame.setVisible(false);
		}
	}

	public void openAddUserFrame() {
		this.addUserFrame = new AddUserFrame(this, user, contactGroups);
	}
	
	public void findUserList(Map<String, String> requirement){
		Message message = new Message(8, requirement);
		thread.sendMessage(message);
	}
	
	public void acceptUserList(Message message){
		List<User> userList = (List<User>) message.getContent();
		this.addUserFrame.refreshUserList(userList);
	}

	public void completeFriends(ChatWord request, ChatWord response) {
		Map<String, ChatWord> friendsMap = new HashMap<String, ChatWord>();
		friendsMap.put("request", request);
		friendsMap.put("response", response);
		Message message = new Message(16, friendsMap);
		thread.sendMessage(message);
	}
	
	public void refreshContacts(Message message){
		Contact contact = (Contact) message.getContent();
		for (ContactGroup contactGroup : contactGroups) {
			if(contactGroup.getGroupId() == contact.getGroupId()){
				contactGroup.getContactList().add(contact);
				this.clientFrame.addContact(contact);
				return;
			}
		}
		ContactGroup contactGroup = new ContactGroup();
		contactGroup.setGoupName(contact.getGroupName());
		contactGroup.setGroupId(contact.getGroupId());
		List<Contact> list = new ArrayList<Contact>();
		list.add(contact);
		contactGroup.setContactList(list);
		this.clientFrame.addContactGroup(contactGroup);
	}

}
