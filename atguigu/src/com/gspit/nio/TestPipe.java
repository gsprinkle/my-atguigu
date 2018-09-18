package com.gspit.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.Date;

import org.junit.Test;

/**
 * 管道是2个线程之间的单向数据连接。
 * Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取
 * @author gsprinkle
 *
 * 2018年9月17日-下午3:59:58
 */
public class TestPipe {
	
	@Test
	public void test() throws IOException{
		// 创建管道
		Pipe pipe = Pipe.open();
		// 向管理中写入数据
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.clear();
		buf.put((new Date() + "\tNow.").getBytes());
		buf.flip();
		SinkChannel sink = pipe.sink();
		while(buf.hasRemaining()){
			sink.write(buf);
		}
		// 读取数据 显示数据
		SourceChannel source = pipe.source();
		ByteBuffer buf2 = ByteBuffer.allocate(1024);
		source.read(buf2);
		
		buf2.flip();
		System.out.println(new String(buf2.array(),0,buf2.limit()));
		
	}
	
}
