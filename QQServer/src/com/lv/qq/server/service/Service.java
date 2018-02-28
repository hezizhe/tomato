package com.lv.qq.server.service;

import java.util.List;
import java.util.Map;

import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.User;
import com.lv.qq.server.dao.ContactDto;

public interface Service {

	public Map<String, Object> login(User user);

	public void outOfLine(long account);

	public List<ContactGroup> getContactGroups(long account);
	
	public List<ChatWord> getUnreadMessages(long account);

	public void addUnread(ChatWord chatWord);

	public void removeUnread(long talker, long listener);

	public void removeUnreadById(long account, String id);

	public boolean updateUser(User newUser);

	public User findByAccount(long account);

	public User register(User user);

	public Map<String, List<Dictionary>> getDictionarys();

	public List<User> findUserList(Map<String, String> requirement);

	public Contact addContact(long account, ContactDto contact);
}
