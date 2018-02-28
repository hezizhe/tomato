package com.lv.qq.server.test;

import java.util.List;
import java.util.Map;

import com.lv.qq.common.vo.Contact;
import com.lv.qq.common.vo.User;
import com.lv.qq.server.dao.ContactDto;
import com.lv.qq.server.dao.UserDaoImpl;
import com.lv.qq.server.dao.UserDao;
import com.lv.qq.server.service.Service;
import com.lv.qq.server.service.ServiceImpl;

public class Test {
	public static void main(String[] args) {
		Service a = new ServiceImpl();
		a.addContact(1000020, new ContactDto(1000003, "ͼ3", "gu"));
	}
}
