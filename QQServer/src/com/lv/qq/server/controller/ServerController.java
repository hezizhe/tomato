package com.lv.qq.server.controller;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.Message;
import com.lv.qq.common.vo.User;
import com.lv.qq.server.controller.thread.CommunicationThread;
import com.lv.qq.server.controller.thread.FileForwordThread;
import com.lv.qq.server.controller.thread.MainThread;
import com.lv.qq.server.dao.ContactDto;
import com.lv.qq.server.service.Service;
import com.lv.qq.server.service.ServiceImpl;

@SuppressWarnings("unchecked")
public class ServerController {
	
	public static Map<Long, CommunicationThread> THREAD_MANAGER = new HashMap<Long, CommunicationThread>();
	
	public static Map<String, FileForwordThread> UPLOAD_THREAD_MANAGER = new HashMap<String, FileForwordThread>();
	
	private MainThread mainThread = new MainThread(this);
	
	private Service service = new ServiceImpl();
	
	private static Map<Integer, String> METHOD_MAP;
	
	static{
		METHOD_MAP = new HashMap<Integer, String>();
		METHOD_MAP.put(0, "login");
		METHOD_MAP.put(1, "newAccount");
		METHOD_MAP.put(2, "register");
		METHOD_MAP.put(3, "updateUser");
		METHOD_MAP.put(4, "getDictionarys");
		METHOD_MAP.put(6, "getUser");
		METHOD_MAP.put(8, "findUserList");
		METHOD_MAP.put(9, "forword");
		METHOD_MAP.put(10, "hadRead");
		METHOD_MAP.put(11, "acceptFile");
		METHOD_MAP.put(14, "cancerTrans");
		METHOD_MAP.put(16, "completeFriends");
		METHOD_MAP.put(17, "hadReadOne");
	}
	
	public void startMainThread(){
		mainThread.start();
	}

	public Message dealWith(Message message) {
		Message returnMessage = null;
		String methodName = METHOD_MAP.get(message.getType());
		try {
			Method method = this.getClass().getMethod(methodName, Message.class);
			Object obj = method.invoke(this, message);
			returnMessage = obj == null ? null : (Message)obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMessage;
	}
	
	public Message login(Message message){
		User user = (User) message.getContent();
		Map<String, Object> resultMap = service.login(user);
		//群发上线通知
		if((int)resultMap.get("result") == 1){
			List<ContactGroup> contactGroups = (List<ContactGroup>) resultMap.get("contactGroups");
			User u = (User) resultMap.get("user");
			for (ContactGroup contactGroup : contactGroups) {
				for (Contact contact : contactGroup.getContactList()) {
					CommunicationThread thread = THREAD_MANAGER.get(contact.getAccount());
					if(thread != null){
						Map<String, Long> statusMap = new HashMap<String, Long>();
						statusMap.put("account", u.getAccount());
						statusMap.put("status", (long)u.getStatus());
						thread.sendMessage(new Message(3, statusMap));
					}
				}
			}
		}
		return new Message(0, resultMap);
	}
	
	public Message register(Message message){
		User user = (User)message.getContent();
		user = service.register(user);
		user = service.findByAccount(user.getAccount());
		return new Message(2, user);
	}
	
	public Message updateUser(Message message){
		User newUser = (User)message.getContent();
		boolean result = service.updateUser(newUser);
		User user = service.findByAccount(newUser.getAccount());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("user", user);
		return new Message(5, map);
	}
	
	public Message getDictionarys(Message message){
		Map<String, List<Dictionary>> dictionarys = service.getDictionarys();
		Message reply = new Message(4, dictionarys);
		return reply;
	}
	
	public Message getUser(Message message){
		long account = (long) message.getContent();
		User user = service.findByAccount(account);
		return new Message(6, user);
	}
	
	public Message findUserList(Message message){
		Map<String, String> requirement = (Map<String, String>)message.getContent();
		List<User> userList = service.findUserList(requirement);
		return new Message(8, userList);
	}
	
	public Message forword(Message message){
		ChatWord chatWord = (ChatWord) message.getContent();
		//把消息存入对方数据库未读列表中
		service.addUnread(chatWord);
		
		CommunicationThread thread = THREAD_MANAGER.get(chatWord.getListener());
		if(thread != null){
			//向对方发送消息
			thread.sendMessage(new Message(10, chatWord));
		}
		//只要能到达服务器就代表消息发送成功
		return new Message(9, chatWord);
	}
	
	public Message hadRead(Message message){
		Map<String, Long> map = (Map<String, Long>) message.getContent();
		long talker = ((Long)map.get("talker")).longValue();
		long listener = ((Long)map.get("listener")).longValue();
		service.removeUnread(talker, listener);
		return null;
	}
	
	public Message hadReadOne(Message message){
		ChatWord chatWord =  (ChatWord) message.getContent();
		service.removeUnreadById(chatWord.getListener(), chatWord.getId());
		return null;
	}

	public void outOfLine(long account) {
		THREAD_MANAGER.remove(account);
		service.outOfLine(account);
		//群发离线通知
		List<ContactGroup> contactGroups = service.getContactGroups(account);
		for (ContactGroup contactGroup : contactGroups) {
			for (Contact contact : contactGroup.getContactList()) {
				CommunicationThread thread = THREAD_MANAGER.get(contact.getAccount());
				if(thread != null){
					thread.sendMessage(new Message(4, account));
				}
			}
		}
	}
	
	public Message acceptFile(Message message){
		return message;
	}
	
	public Message cancerTrans(Message message){
		Map<String, Object> map = (Map<String, Object>)message.getContent();
		ChatWord chatWord = (ChatWord) map.get("chatWord");
		FileForwordThread fft = UPLOAD_THREAD_MANAGER.get(chatWord.getId());
		fft.cancer();
		UPLOAD_THREAD_MANAGER.remove(chatWord.getId());
		service.removeUnreadById(chatWord.getListener(), chatWord.getId());
		CommunicationThread threadL = THREAD_MANAGER.get(chatWord.getListener());
		if(threadL != null)
			threadL.sendMessage(message);
		CommunicationThread threadT = THREAD_MANAGER.get(chatWord.getTalker());
		threadT.sendMessage(message);
		return null;
	}
	
	public Message completeFriends(Message message){
		Map<String, ChatWord> friendsMap = (Map<String, ChatWord>)message.getContent();
		ChatWord request = friendsMap.get("request");
		ChatWord response = friendsMap.get("response");
		ContactDto reqDto = new ContactDto(request.getListener(), request.getRemarks(), request.getGroupName());
		Contact reqContact = service.addContact(request.getTalker(), reqDto);
		ContactDto rspDto = new ContactDto(response.getListener(), response.getRemarks(), response.getGroupName());
		Contact rsqContact = service.addContact(response.getTalker(), rspDto);
		ChatWord systemWord = new ChatWord(request.getTalker(), request.getRemarks() + "同意了你的添加好友申请");
		service.addUnread(systemWord);
		CommunicationThread thread = THREAD_MANAGER.get(request.getTalker());
		if(thread != null){
			thread.sendMessage(new Message(10, systemWord));
			thread.sendMessage(new Message(16, reqContact));
		}
		CommunicationThread thread2 = THREAD_MANAGER.get(response.getTalker());
		if(thread2 != null){
			thread2.sendMessage(new Message(16, rsqContact));
		}
		return null;
	}

}
