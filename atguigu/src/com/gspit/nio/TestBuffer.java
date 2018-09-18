package com.gspit.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

/*
 * 一、缓冲区（Buffer）：在 Java NIO 中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 * 
 * 根据数据类型不同（boolean 除外），提供了相应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 * 
 * 上述缓冲区的管理方式几乎一致，通过 allocate() 获取缓冲区
 * 
 * 二、缓冲区存取数据的两个核心方法：
 * put() : 存入数据到缓冲区中
 * get() : 获取缓冲区中的数据
 * 
 * 三、缓冲区中的四个核心属性：
 * capacity : 容量，表示缓冲区中最大存储数据的容量。一旦声明不能改变。
 * limit : 界限，表示缓冲区中可以操作数据的大小。（limit 后数据不能进行读写）
 * position : 位置，表示缓冲区中正在操作数据的位置。
 * 
 * mark : 标记，表示记录当前 position 的位置。可以通过 reset() 恢复到 mark 的位置
 * 
 * 0 <= mark <= position <= limit <= capacity
 * 
 * 四、直接缓冲区与非直接缓冲区：
 * 非直接缓冲区：通过 allocate() 方法分配缓冲区，将缓冲区建立在 JVM 的内存中
 * 直接缓冲区：通过 allocateDirect() 方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
 */
public class TestBuffer {
	
	@Test
	public void test3(){
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		System.out.println(buf.isDirect());
	}
	
	
	@Test
	public void test2(){
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put("abcde".getBytes());
		buf.flip();
		byte[] dst = new byte[buf.limit()];
		buf.get(dst, 0, 2);
		buf.mark();
		printResult(buf);
		buf.get(dst,2,2);
		printResult(buf);
		buf.reset();
		printResult(buf);
		int remaining = buf.remaining();
		System.out.println("remaining:" + remaining);
		
	}

	@Test
	public void test1() {
		// 1. 分配一个指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);

		// 2. 利用 put() 存入数据到缓冲区中
		System.out.println("----------put-----------");
		buf.put("abcde".getBytes());
		printResult(buf);

		// 3. 切换读取数据模式
		System.out.println("----------flip-----------");
		buf.flip();// 切换至读模式
		printResult(buf);

		// 4. 利用 get() 读取缓冲区中的数据
		System.out.println("----------get-----------");
		byte[] dst = new byte[buf.limit()];
		buf.get(dst);
		System.out.println("读到的数据：" + new String(dst, 0, dst.length));
		printResult(buf);

		// 5. rewind():可重复读
		System.out.println("----------rewind-----------");
		buf.rewind();
		printResult(buf);

		// 6. clear() : 清空缓冲区. 但是缓冲区中的数据依然存在，但是处于“被遗忘”状态
		System.out.println("----------clear-----------");
		buf.clear();
		printResult(buf);

	}

	public void printResult(ByteBuffer buf) {
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());
	}
}
