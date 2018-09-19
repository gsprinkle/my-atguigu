package com.gspit.gmall;

import java.io.IOException;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gspit.gmall.bean.UserAddress;
import com.gspit.gmall.service.OrderService;

public class MainApplication {
	
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		OrderService orderService = context.getBean(OrderService.class);
		List<UserAddress> list = orderService.initOrder("1");
		System.out.println(list);
		System.in.read();
	}
}
