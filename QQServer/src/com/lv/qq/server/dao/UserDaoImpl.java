package com.lv.qq.server.dao;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.ContactGroup;
import com.lv.qq.common.vo.Dictionary;
import com.lv.qq.common.vo.User;
import com.lv.qq.util.StringTools;

public class UserDaoImpl implements UserDao {
	
	private static String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static String URL = "jdbc:mysql://127.0.0.1:3306/qq?useUnicode=true&amp;characterEncoding=UTF-8";
	private static String USER = "root";
	private static String PASSWORD = "root";
	
	private Connection conn;
	private PreparedStatement prst;
	private ResultSet rs;
	
	public boolean getConnection(){
		try {
			Class.forName(DRIVER_CLASS);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void closeResouce(){
		try {
			if(rs!=null)
				rs.close();
			if(prst!=null)
				prst.close();
			if(conn!=null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public User findByAccount(Long account) {
		String sql = "select account, user_name, password, gender, status, signature,"
					+ " birthday, blood_type, hometown, location, phone, email, school,"
					+ " profession, company, introduce from user where account = " + account;
		User user = null;
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				rs = prst.executeQuery();
				while(rs.next()){
					user = new User();
					user.setAccount(rs.getLong("account"));
					user.setUserName(rs.getString("user_name"));
					user.setPassword(rs.getString("password"));
					user.setGender(rs.getInt("gender"));
					user.setStatus(rs.getInt("status"));
					user.setSignature(rs.getString("signature"));
					user.setBirthday(rs.getTimestamp("birthday"));
					user.setBloodType(rs.getInt("blood_type"));
					user.setHometown(rs.getString("hometown"));
					user.setLocation(rs.getString("location"));
					user.setPhone(rs.getString("phone"));
					user.setEmail(rs.getString("email"));
					user.setSchool(rs.getString("school"));
					user.setProfession(rs.getInt("profession"));
					user.setCompany(rs.getString("company"));
					user.setIntroduce(rs.getString("introduce"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return user;
	}

	@Override
	public List<ContactGroup> getContactGroups(Long account) {
		List<ContactGroup> groups = new ArrayList<ContactGroup>();
		String sql = "select group_id, group_name from contact_group_" + account + " group by group_id";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				rs = prst.executeQuery();
				while(rs.next()){
					ContactGroup group = new ContactGroup();
					group.setGroupId(rs.getInt("group_id"));
					group.setGoupName(rs.getString("group_name"));
					groups.add(group);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return groups;
	}
	
	@Override
	public List<Long> getAllContactAccount(Long account) {
		List<Long> accounts = new ArrayList<Long>();
		String sql = "select account from contact_group_" + account;
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				rs = prst.executeQuery();
				while(rs.next()){
					accounts.add(rs.getLong("account"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return accounts;
	}
	
	@Override
	public List<Contact> getContactsByGroup(Long account, int groupId){
		List<Contact> contactList = new ArrayList<Contact>();
		String sql = "select  c.account as account, c.remarks, c.group_id, u.user_name, u.gender, u.status, u.signature from contact_group_" + account
						+ " c, user u where c.account = u.account and c.group_id = ?";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setInt(1, groupId);
				rs = prst.executeQuery();
				while(rs.next()){
					Contact contact = new Contact();
					contact.setAccount(rs.getLong("account"));
					contact.setRemarks(rs.getString("remarks"));
					contact.setGroupId(rs.getInt("group_id"));
					contact.setUserName(rs.getString("user_name"));
					contact.setGender(rs.getInt("gender"));
					contact.setStatus(rs.getInt("status"));
					contact.setSignature(rs.getString("signature"));
					contactList.add(contact);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return contactList;
	}
	
	@Override
	public void updateStatus(User user){
		String sql = "update user set status = ? where account = ?";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setInt(1, user.getStatus());
				prst.setLong(2, user.getAccount());
				prst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
	}

	@Override
	public void addUnread(ChatWord chatWord) {
		long account = chatWord.getListener();
		String sql = "insert into unread_message_" + account + "(id, word, talker, font_name, font_style, "
				+ "font_size, time, type, file_path, file_name, file_length, remarks, group_name)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setString(1, chatWord.getId());
				prst.setString(2, chatWord.getWord() == null ? "" : chatWord.getWord());
				prst.setLong(3, chatWord.getTalker());
				prst.setString(4, chatWord.getFont() == null ? "" : chatWord.getFont().getName());
				prst.setInt(5, chatWord.getFont() == null ? 0 : chatWord.getFont().getStyle());
				prst.setInt(6, chatWord.getFont() == null ? 0 : chatWord.getFont().getSize());
				prst.setTimestamp(7, new Timestamp(chatWord.getTime().getTime()));
				prst.setInt(8, chatWord.getType());
				prst.setString(9, chatWord.getFilePath() == null ? "" : chatWord.getFilePath());
				prst.setString(10, chatWord.getFileName() == null ? "" : chatWord.getFileName());
				prst.setLong(11, chatWord.getFileLength());
				prst.setString(12, chatWord.getRemarks() == null ? "" : chatWord.getRemarks());
				prst.setString(13, chatWord.getGroupName() == null ? "" : chatWord.getGroupName());
				prst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
	}

	@Override
	public void removeUnread(long talker, long listener) {
		String sql = "delete from unread_message_" + listener + " where talker = ?";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setLong(1, talker);
				prst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
	}

	@Override
	public List<ChatWord> getUnreadMessages(long account) {
		List<ChatWord> unreadMessages = new ArrayList<ChatWord>();
		String sql = "select id, word, talker, font_name, font_style, font_size, time, type, file_path, "
							+ "file_name, file_length, remarks, group_name from unread_message_"
							+ account + " order by time ";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				rs = prst.executeQuery();
				while(rs.next()){
					ChatWord chatWord = new ChatWord();
					chatWord.setListener(account);
					chatWord.setId(rs.getString("id"));
					chatWord.setWord(rs.getString("word"));
					chatWord.setTalker(rs.getLong("talker"));
					String name = rs.getString("font_name");
					int style = rs.getInt("font_style");
					int size = rs.getInt("font_size");
					chatWord.setFont(new Font(name, style, size));
					chatWord.setTime(rs.getTimestamp("time"));
					chatWord.setType(rs.getInt("type"));
					chatWord.setFilePath(rs.getString("file_path"));
					chatWord.setFileName(rs.getString("file_name"));
					chatWord.setFileLength(rs.getLong("file_length"));
					chatWord.setRemarks(rs.getString("remarks"));
					chatWord.setGroupName(rs.getString("group_name"));
					unreadMessages.add(chatWord);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return unreadMessages;
	}

	@Override
	public void removeUnreadById(long account, String id) {
		String sql = "delete from unread_message_" + account + " where id = ?";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setString(1, id);
				prst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
	}

	@Override
	public boolean updateUser(User newUser) {
		String sql = "update user set user_name = ?, password = ?, gender = ?, signature = ?,"
						+ " birthday = ?, blood_type = ?, hometown = ?, location = ?, phone = ?, "
						+ "email = ?, school = ?, profession = ?, company = ?, introduce = ? "
						+ "where account = ?";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setString(1, newUser.getUserName());
				prst.setString(2, newUser.getPassword());
				prst.setInt(3, newUser.getGender());
				prst.setString(4, newUser.getSignature());
				prst.setTimestamp(5, new Timestamp(newUser.getBirthday().getTime()));
				prst.setInt(6, newUser.getBloodType());
				prst.setString(7, newUser.getHometown());
				prst.setString(8, newUser.getLocation());
				prst.setString(9, newUser.getPhone());
				prst.setString(10, newUser.getEmail());
				prst.setString(11, newUser.getSchool());
				prst.setInt(12, newUser.getProfession());
				prst.setString(13, newUser.getCompany());
				prst.setString(14, newUser.getIntroduce());
				prst.setLong(15, newUser.getAccount());
				int updateLine = prst.executeUpdate();
				if(updateLine > 0)
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return false;
	}

	@Override
	public User register(User user) {
		try {
			if (getConnection()) {
				conn.setAutoCommit(false);
				String registerSql = "insert into user (user_name, password) values(?, ?)";
				prst = conn.prepareStatement(registerSql);
				prst.setString(1, user.getUserName());
				prst.setString(2, user.getPassword());
				prst.executeUpdate();
				String getIdSql = "SELECT LAST_INSERT_ID()";
				prst = conn.prepareStatement(getIdSql);
				rs = prst.executeQuery();
				long account = 0;
				while(rs.next()){
					account = rs.getLong(1);
				}
				if(account == 0){
					conn.rollback();
				}else{
					String createSql1 = "CREATE TABLE `contact_group_" + account + "` (`account` bigint(255) NOT NULL, "
										+ "`remarks` varchar(255) DEFAULT NULL, `group_id` int(10) NOT NULL DEFAULT '0', "
										+ "`group_name` varchar(255) NOT NULL, PRIMARY KEY (`account`))";
					String createSql2 = "CREATE TABLE `unread_message_" + account + "` (`id` varchar(255) NOT NULL, "
										+ "`word` varchar(255) DEFAULT NULL,`talker` bigint(20) NOT NULL,"
										+ "`font_name` varchar(255) DEFAULT NULL,`font_style` tinyint(5) DEFAULT NULL,"
										+ "`font_size` tinyint(10) DEFAULT NULL,`time` datetime NOT NULL,"
										+ "`type` tinyint(5) NOT NULL DEFAULT '1',`file_path` varchar(255) DEFAULT NULL,"
										+"`file_name` varchar(255) DEFAULT NULL,`file_length` bigint(20) DEFAULT NULL,"
										+"`remarks` varchar(255) DEFAULT NULL,`group_name` varchar(255) DEFAULT NULL,"
										+ "PRIMARY KEY (`id`))";
					prst = conn.prepareStatement(createSql1);
					prst.executeUpdate();
					prst = conn.prepareStatement(createSql2);
					prst.executeUpdate();
					conn.commit();
				}
				user.setAccount(account);
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return user;
	}

	@Override
	public List<Dictionary> getDictionaryByType(String type) {
		List<Dictionary> dictionary = new ArrayList<Dictionary>();
		String sql = "select name, value, type from dictionary where type = ? ";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setString(1, type);
				rs = prst.executeQuery();
				while(rs.next()){
					Dictionary dic = new Dictionary();
					dic.setName(rs.getString("name"));
					dic.setValue(rs.getInt("value"));
					dic.setType(rs.getString("type"));
					dictionary.add(dic);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return dictionary;
	}

	@Override
	public List<User> findUserList(Map<String, String> requirement,
			List<Long> accounts) {
		String except = requirement.get("account");
		for (Long account : accounts) {
			except = except + "," + account;
		}
		String requirs = "";
		if(requirement.get("hometown") != null && !requirement.get("hometown").trim().equals("")){
			requirs += " and hometown = '" + requirement.get("hometown") + "' ";
		}
		if(requirement.get("location") != null && !requirement.get("location").trim().equals("")){
			requirs += " and location = '" + requirement.get("location") + "' ";
		}
		if(requirement.get("status") != null && !requirement.get("status").trim().equals("")){
			requirs += " and status = " + requirement.get("status");
		}
		if(requirement.get("gender") != null && !requirement.get("gender").trim().equals("")){
			requirs += " and gender = " + requirement.get("gender");
		}
		if(requirement.get("birthdayMax") != null && !requirement.get("birthdayMax").trim().equals("")){
			requirs += " and birthday < '" + requirement.get("birthdayMax") + "' ";
		}
		if(requirement.get("birthdayMin") != null && !requirement.get("birthdayMin").trim().equals("")){
			requirs += " and birthday > '" + requirement.get("birthdayMin") + "' ";
		}
		if(requirement.get("keyWord") != null && !requirement.get("keyWord").trim().equals("")){
			String keyWord = requirement.get("keyWord");
			if(StringTools.isPosiInt(keyWord)){
				requirs += " and ( account = " + keyWord + " or phone = " + keyWord + " ) ";
			}else {
				requirs += " and ( user_name like '%" + keyWord + "%' or email like '%" + keyWord + "%') ";
			}
		}
		String sql = "select account, user_name, gender, status, signature,"
					+ " birthday, blood_type, hometown, location, phone, email, school,"
					+ " profession, company, introduce from user where account not in ("
					+ except + ") " + requirs + " limit " + requirement.get("limit");
		List<User> userList = new ArrayList<User>();
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				rs = prst.executeQuery();
				while(rs.next()){
					User user = new User();
					user.setAccount(rs.getLong("account"));
					user.setUserName(rs.getString("user_name"));
					user.setGender(rs.getInt("gender"));
					user.setStatus(rs.getInt("status"));
					user.setSignature(rs.getString("signature"));
					user.setBirthday(rs.getTimestamp("birthday"));
					user.setBloodType(rs.getInt("blood_type"));
					user.setHometown(rs.getString("hometown"));
					user.setLocation(rs.getString("location"));
					user.setPhone(rs.getString("phone"));
					user.setEmail(rs.getString("email"));
					user.setSchool(rs.getString("school"));
					user.setProfession(rs.getInt("profession"));
					user.setCompany(rs.getString("company"));
					user.setIntroduce(rs.getString("introduce"));
					userList.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return userList;
	}

	@Override
	public Integer getGroupIdByName(long account, String groupName) {
		String sql = "select group_id from contact_group_" + account
						+ " where group_name = ? group by group_id";
		System.out.println(sql);
		Integer groupId = null;
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setString(1, groupName);
				rs = prst.executeQuery();
				while(rs.next()){
					groupId = rs.getInt("group_id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return groupId;
	}

	@Override
	public Integer getMaxGroupId(long account) {
		String sql = "select max(group_id) as group_id from contact_group_" + account;
		System.out.println(sql);
		Integer groupId = null;
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				rs = prst.executeQuery();
				while(rs.next()){
					groupId = rs.getInt("group_id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return groupId;
	}

	@Override
	public void addContact(long account, ContactDto contact) {
		String sql = "insert into contact_group_" + account + " (account, remarks, group_id, group_name) values(?, ?, ?, ?)";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setLong(1, contact.getAccount());
				prst.setString(2, contact.getRemarks());
				prst.setInt(3, contact.getGroupId());
				prst.setString(4, contact.getGroupName());
				prst.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
	}
	
	@Override
	public Contact getContactByAccount(Long uaccount, Long caccount){
		Contact contact = null;
		String sql = "select  c.account as account, c.remarks, c.group_name, c.group_id, u.user_name,"
						+ " u.gender, u.status, u.signature from contact_group_" + uaccount
						+ " c, user u where c.account = u.account and c.account = ?";
		System.out.println(sql);
		try {
			if (getConnection()) {
				prst = conn.prepareStatement(sql);
				prst.setLong(1, caccount);
				rs = prst.executeQuery();
				while(rs.next()){
					contact = new Contact();
					contact.setAccount(rs.getLong("account"));
					contact.setRemarks(rs.getString("remarks"));
					contact.setGroupName(rs.getString("group_name"));
					contact.setGroupId(rs.getInt("group_id"));
					contact.setUserName(rs.getString("user_name"));
					contact.setGender(rs.getInt("gender"));
					contact.setStatus(rs.getInt("status"));
					contact.setSignature(rs.getString("signature"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResouce();
		}
		return contact;
	}
	
}
