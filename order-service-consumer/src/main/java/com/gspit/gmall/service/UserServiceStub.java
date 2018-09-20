package com.gspit.gmall.service;

import java.util.List;

import org.springframework.util.StringUtils;

import com.gspit.gmall.bean.UserAddress;

public class UserServiceStub implements UserService{
	
	private UserService userService;
	
	public UserServiceStub(UserService userService) {
		this.userService = userService;
	}

	@Override
	public List<UserAddress> getUserAddressList(String userId) {
		System.out.println("UserServiceStub...");
		if(!StringUtils.isEmpty(userId)){
			return userService.getUserAddressList(userId);
		}
		return null;
	}
	
}
