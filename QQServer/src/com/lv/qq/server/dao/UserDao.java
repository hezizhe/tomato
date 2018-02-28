package com.lv.qq.server.dao;

import java.util.List;
import java.util.Map;

import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.User;

public interface UserDao {
	
	public User findByAccount(Long account);
	
	public List<ContactGroup> getContactGroups(Long account);

	public List<Contact> getContactsByGroup(Long account, int groupId);
	
	public void updateStatus(User user);

	public void addUnread(ChatWord chatWord);

	public void removeUnread(long talker, long listener);

	public List<ChatWord> getUnreadMessages(long account);

	public void removeUnreadById(long account, String id);

	public boolean updateUser(User newUser);

	public User register(User user);

	public List<Dictionary> getDictionaryByType(String type);

	public List<Long> getAllContactAccount(Long account);

	public List<User> findUserList(Map<String, String> requirement,
			List<Long> accounts);

	public Integer getGroupIdByName(long account, String groupName);

	public Integer getMaxGroupId(long account);

	public void addContact(long account, ContactDto contact);

	public Contact getContactByAccount(Long uaccount, Long caccount);
}
