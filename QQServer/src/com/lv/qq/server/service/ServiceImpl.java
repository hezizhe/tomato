package com.lv.qq.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.User;
import com.lv.qq.server.dao.ContactDto;
import com.lv.qq.server.dao.UserDao;
import com.lv.qq.server.dao.UserDaoImpl;

public class ServiceImpl implements Service {
	
	private UserDao userDao = new UserDaoImpl();

	@Override
	public Map<String, Object> login(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		User u = userDao.findByAccount(user.getAccount());
		if(u == null || !user.getPassword().equals(u.getPassword())){
			map.put("result", 0);
			return map;
		}
		u.setStatus(1);
		userDao.updateStatus(u);
		map.put("result", 1);
		map.put("user", u);
		map.put("contactGroups", getContactGroups(u.getAccount()));
		map.put("unreadMessages", getUnreadMessages(u.getAccount()));
		return map;
	}
	
	@Override
	public User findByAccount(long account){
		return userDao.findByAccount(account);
	}

	@Override
	public void outOfLine(long account) {
		User user = new User();
		user.setAccount(account);
		user.setStatus(0);
		userDao.updateStatus(user);
	}
	
	@Override
	public List<ContactGroup> getContactGroups(long account){
		List<ContactGroup> contactGroups = userDao.getContactGroups(account);
		for (ContactGroup contactGroup : contactGroups) {
			List<Contact> contactList = userDao.getContactsByGroup(account, contactGroup.getGroupId());
			contactGroup.setContactList(contactList);
		}
		return contactGroups;
	}
	
	@Override
	public List<ChatWord> getUnreadMessages(long account) {
		return userDao.getUnreadMessages(account);
	}

	@Override
	public void addUnread(ChatWord chatWord) {
		userDao.addUnread(chatWord);
	}

	@Override
	public void removeUnread(long talker, long listener) {
		userDao.removeUnread(talker, listener);
	}

	@Override
	public void removeUnreadById(long account, String id) {
		userDao.removeUnreadById(account, id);
	}

	@Override
	public boolean updateUser(User newUser) {
		return userDao.updateUser(newUser);
	}

	@Override
	public User register(User user) {
		return userDao.register(user);
	}

	@Override
	public Map<String, List<Dictionary>> getDictionarys() {
		Map<String, List<Dictionary>> dictionarys = new HashMap<String, List<Dictionary>>();
		dictionarys.put("gender", userDao.getDictionaryByType("gender"));
		dictionarys.put("blood_type", userDao.getDictionaryByType("blood_type"));
		dictionarys.put("profession", userDao.getDictionaryByType("profession"));
		return dictionarys;
	}

	@Override
	public List<User> findUserList(Map<String, String> requirement) {
		long account = Long.valueOf(requirement.get("account"));
		List<Long> accounts = userDao.getAllContactAccount(account);
		return userDao.findUserList(requirement, accounts);
	}

	@Override
	public Contact addContact(long account, ContactDto contact) {
		Integer groupId = userDao.getGroupIdByName(account, contact.getGroupName());
		if(groupId == null){
			Integer maxGroupId = userDao.getMaxGroupId(account);
			groupId = maxGroupId == null ? 0 : maxGroupId + 1;
		}
		contact.setGroupId(groupId);
		userDao.addContact(account, contact);
		return userDao.getContactByAccount(account, contact.getAccount());
	}

}
