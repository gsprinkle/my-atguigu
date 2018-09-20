package com.gspit.gmall.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gspit.gmall.bean.UserAddress;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class OrderServiceImpl implements OrderService {
	@Reference  // 引用远程服务
	private UserService userService;

	@HystrixCommand(fallbackMethod="hello")
	@Override
	public List<UserAddress> initOrder(String userId) {
		List<UserAddress> userAddressList = userService.getUserAddressList(userId);
		return userAddressList;
	}
	
	public List<UserAddress> hello(String userId) {
		UserAddress userAddress = new UserAddress(1,"测试","1","测试","测试","Y");
		 return Arrays.asList(userAddress);
	}

}
