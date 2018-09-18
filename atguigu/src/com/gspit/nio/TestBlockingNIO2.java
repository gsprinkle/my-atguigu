package com.gspit.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/*
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
 * 案例：客户端发送一个文件，服务端接收并保存该文件！
 */
public class TestBlockingNIO2 {
	
	@Test
	public void client() throws IOException{
		// 1. 创建一个通道
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		// 2. 创建一个缓冲区
		ByteBuffer bBuf = ByteBuffer.allocate(1024);
		// 3. 创建一个用于发送的通道
		FileChannel inChannel = FileChannel.open(Paths.get("E:/soap.xml"), StandardOpenOption.READ);
		// 4. 把数据写入SocketChannel
		while(inChannel.read(bBuf) != -1){
			bBuf.flip();
			sChannel.write(bBuf);
			bBuf.clear();
		}
		
		// 结束发送数据的连接,如果不结束，就会造成阻塞，永远接收不到服务端的反馈
		sChannel.shutdownOutput();
		
		// 5. 接受服务端的反馈
		int len =0;
		while((len = sChannel.read(bBuf)) != -1){
			bBuf.flip();
			System.out.println(new String(bBuf.array(),0,len));
			bBuf.clear();
		}
		// 6. 关闭通道
		inChannel.close();
		sChannel.close();
		
	}
	
	@Test 
	public void server() throws IOException{
		// 1. 创建一个服务端通道,绑定端口号，并接收客户端的连接
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.bind(new InetSocketAddress(9898));
		SocketChannel sChannel = ssChannel.accept();
		// 2. 创建一个缓冲区
		ByteBuffer bBuf = ByteBuffer.allocate(1024);
		
		// 3. 读取客户端发送来的数据,并存入文件
		FileChannel outChannel = FileChannel.open(Paths.get("E:/soap2.xml"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		while(sChannel.read(bBuf) != -1){
			bBuf.flip();
			outChannel.write(bBuf);
			bBuf.clear();
		}
		// 4. 给客户端反馈一条消息
		bBuf.put("文件已收到".getBytes());
		bBuf.flip();
		sChannel.write(bBuf);
		// 5. 关闭资源
		outChannel.close();
		sChannel.close();
		ssChannel.close();
	}
}
