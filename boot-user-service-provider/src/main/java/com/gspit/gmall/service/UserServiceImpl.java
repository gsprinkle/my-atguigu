package com.gspit.gmall.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.gspit.gmall.bean.UserAddress;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service // 显露服务
@Component
public class UserServiceImpl implements UserService {

	@HystrixCommand
	@Override
	public List<UserAddress> getUserAddressList(String userId) {
		List<UserAddress> list = new ArrayList<UserAddress>();
		if(Math.random() > 0.5){
			throw new RuntimeException();
		}
		UserAddress address1 = new UserAddress(1, "北京市昌平区宏福科技园综合楼3层", "1", "李老师", "010-56253825", "Y");
		UserAddress address2 = new UserAddress(2, "深圳市宝安区西部硅谷大厦B座3层（深圳分校）", "1", "王老师", "010-56253825", "N");
		list.add(address1);
		list.add(address2);
		return list; 
	}

}
