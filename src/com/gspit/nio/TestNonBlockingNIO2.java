package com.gspit.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

public class TestNonBlockingNIO2 {
	
	@Test
	public void send() throws IOException{
		// 创建一个channel
		DatagramChannel dc = DatagramChannel.open();
		// 切换成非阻塞模式
		dc.configureBlocking(false);
		// 创建一个缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		// 缓冲区添加一条数据
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()){
			String str = scanner.nextLine();
			buf.put((new Date() + "\t" + str).getBytes());
			buf.flip();
			dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
			buf.clear();
		}
		dc.close();
	}
	
	@Test
	public void recive() throws IOException{
		// 创建一个通道
		DatagramChannel dc = DatagramChannel.open();
		// 切换为非阻塞模式，绑定一个端口
		dc.configureBlocking(false);
		dc.bind(new InetSocketAddress(9898));
		// 创建一个Selector
		Selector selector = Selector.open();
		// 把通道注册进Selector，指定监听为Accept
		dc.register(selector, SelectionKey.OP_READ);
		// 轮询Selector的key事件
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey sk = it.next();
				if(sk.isReadable()){
					// 创建一个缓冲区
					ByteBuffer buf = ByteBuffer.allocate(1024);
					dc.receive(buf);
					buf.flip();
					System.out.println(new String(buf.array(),0,buf.limit()));
					buf.clear();
				}
			}
			it.remove();
		}
		
		
	}

}
