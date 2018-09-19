package com.gspit.gmall.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gspit.gmall.bean.UserAddress;
import com.gspit.gmall.service.OrderService;
import com.gspit.gmall.service.UserService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private UserService userService;

	@Override
	public List<UserAddress> initOrder(String userId) {
		List<UserAddress> userAddressList = userService.getUserAddressList(userId);
		return userAddressList;
	}

}
