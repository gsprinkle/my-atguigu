package com.gspit.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 
 * 一、使用 NIO 完成网络通信的三个核心：
 * 
 * 1. 通道（Channel）：负责连接
 * 		
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 * 
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 * 
 * 2. 缓冲区（Buffer）：负责数据的存取
 * 
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 * 
 * 案例：使用非阻塞的方式发送消息
 * @author gsprinkle
 *
 * 2018年9月17日-上午10:13:37
 */
public class TestNonBlockingNIO {

	@Test
	public void client() throws IOException{
		// 1. 创建一个通道，连接服务端
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		// 2.创建一个缓冲区，用来存储数据
		ByteBuffer buf = ByteBuffer.allocate(1024);
		// 3. 把通道切换为非阻塞模式
		sChannel.configureBlocking(false);
		// 4. 发送数据
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNext()){
			String str = scanner.nextLine();
			buf.put(str.getBytes());
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		// 5. 关闭资源
		sChannel.close();
		
	}
	
	@Test
	public void server() throws IOException{
		// 1. 创建一个服务端通道，用来接受客户端 的连接
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		// 2. 切换为非阻塞模式
		ssChannel.configureBlocking(false);
		// 3. 绑定端口
		ssChannel.bind(new InetSocketAddress(9898));
		
		// 5. 创建一个Selector选择器
		Selector selector = Selector.open();
		// 6. 将通道注册到选择器上，并指定监听接收事件Selectionkey
		ssChannel.register(selector,SelectionKey.OP_ACCEPT);
		// 7. 轮询的方式遍历Selector上所有已 “准备就绪” 的键
		while(selector.select() > 0){
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()){
				SelectionKey key = iterator.next();
				// 8. 如果是接收事件，获取通道
				if(key.isAcceptable()){
					SocketChannel sChannel = ssChannel.accept();
					// 切换为非阻塞模式
					sChannel.configureBlocking(false);
					// 注册到selector中，指定 SelectionKey 为read
					sChannel.register(selector, SelectionKey.OP_READ);
				}else if(key.isReadable()){// 如果为读取事件，读取其中的内容到缓冲区，并打印
					SocketChannel sChannel = (SocketChannel) key.channel();
					// 4. 创建一个缓冲区
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int len = 0;
					while((len = sChannel.read(buf)) >0){
						System.out.println("-----------------------------");
						buf.flip();
						System.out.println(new String(buf.array(),0,len));
						buf.clear();
					}
				}
				// 取消键
				iterator.remove();
			}
		}
	}
}
