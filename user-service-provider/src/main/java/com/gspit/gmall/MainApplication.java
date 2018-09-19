package com.gspit.gmall;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gspit.gmall.service.UserService;

public class MainApplication {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext content = new ClassPathXmlApplicationContext("provider.xml");
		
		UserService userService = content.getBean(UserService.class);
		
		// 程序暂停，等待用户输入
		System.in.read();
	}

}
