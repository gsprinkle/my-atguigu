package com.gspit.gmall.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gspit.gmall.bean.UserAddress;

@Service
public class OrderServiceImpl implements OrderService {
	@Reference  // 引用远程服务
	private UserService userService;

	@Override
	public List<UserAddress> initOrder(String userId) {
		List<UserAddress> userAddressList = userService.getUserAddressList(userId);
		return userAddressList;
	}

}
