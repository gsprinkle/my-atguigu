package com.gspit.gmall.service;

import java.util.ArrayList;
import java.util.List;

import com.gspit.gmall.bean.UserAddress;

public class UserServiceImpl implements UserService {

	@Override
	public List<UserAddress> getUserAddressList(String userId) {
		List<UserAddress> list = new ArrayList<UserAddress>();
		UserAddress address1 = new UserAddress(1, "北京市昌平区宏福科技园综合楼3层", "1", "李老师", "010-56253825", "Y");
		UserAddress address2 = new UserAddress(2, "深圳市宝安区西部硅谷大厦B座3层（深圳分校）", "1", "王老师", "010-56253825", "N");
		list.add(address1);
		list.add(address2);
		return list; 
	}

}
