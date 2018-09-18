package com.gspit.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Test;

/*
 * 一、通道（Channel）：用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 * 
 * 二、通道的主要实现类
 * 	java.nio.channels.Channel 接口：
 * 		|--FileChannel
 * 		|--SocketChannel
 * 		|--ServerSocketChannel
 * 		|--DatagramChannel
 * 
 * 三、获取通道
 * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * 		本地 IO：
 * 		FileInputStream/FileOutputStream
 * 		RandomAccessFile
 * 
 * 		网络IO：
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 * 		
 * 2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 * 
 * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 * 
 * 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 * 
 * 五、分散(Scatter)与聚集(Gather)
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 * 
 * 六、字符集：Charset
 * 编码：字符串 -> 字节数组
 * 解码：字节数组  -> 字符串
 * 
 */
public class TestChannel {
	
	

	// 5. 字符集
	
	@Test
	public void test6(){
		// Charset
		Charset csetGbk = Charset.forName("GBK");
		CharBuffer cb = CharBuffer.allocate(1024);
		cb.put("郑州中诚燃烧机工程技术有限公司");
		cb.flip();
		// 编码成字节
		ByteBuffer bBuf = csetGbk.encode(cb);
		// 查看编码后的字节
		for(int i=0;i<bBuf.limit();i++){
			//System.out.println(bBuf.get());
		}
		// 用不同的字符集解码
		//Charset csetUtf8 = Charset.forName("UTF-8");
		//CharBuffer cbuf = csetUtf8.decode(bBuf);
		// 正确的解码
		CharBuffer cbuf = csetGbk.decode(bBuf);
		System.out.println(new String(cbuf.array(),0,cbuf.limit()));
	}
	
	@Test
	public void test5() {
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		Set<Entry<String, Charset>> entrySet = charsets.entrySet();
		for (Entry<String, Charset> entry : entrySet) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	// 4. 分散和聚集
	@Test
	public void test4() throws IOException {
		// 创建一个通道，连接一个文件
		FileChannel inChannel = FileChannel.open(Paths.get("E:/soap.xml"), StandardOpenOption.READ);

		// 创建两个缓冲区，分散读取数据
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);

		ByteBuffer[] bufs = { buf1, buf2 };

		inChannel.read(bufs);

		// 查看缓冲区的内容
		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}

		System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
		System.out.println("-----------------------------------------------");
		System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

		// 创建一个输出通道，聚集写入数据
		RandomAccessFile raf = new RandomAccessFile("E:/soap2.xml", "rw");
		FileChannel outChannel = raf.getChannel();
		outChannel.write(bufs);

		outChannel.close();
		raf.close();
		inChannel.close();
	}

	// 3. 通道之间的数据传输(直接缓冲区)
	@Test
	public void test3() throws Exception {
		long start = System.currentTimeMillis();
		FileChannel inChannel = FileChannel.open(Paths.get("F:/BaiduYunDownload/video2.zip"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("F:/BaiduYunDownload/video2-1.zip"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

		outChannel.transferFrom(inChannel, 0, inChannel.size());
		outChannel.close();
		inChannel.close();
		long end = System.currentTimeMillis();
		System.out.println("耗时为：" + (end - start));
	}

	// 2. 利用直接缓冲区完成文件的复制
	@Test
	public void test2() throws Exception {// 1859
		long start = System.currentTimeMillis();
		FileChannel inChannel = FileChannel.open(Paths.get("F:/BaiduYunDownload/video2.zip"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("F:/BaiduYunDownload/video2-1.zip"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
		MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());

		// 直接对缓冲区进行读写操作
		byte[] dst = new byte[inMappedBuf.limit()];
		inMappedBuf.get(dst);
		outMappedBuf.put(dst);

		outChannel.close();
		inChannel.close();
		long end = System.currentTimeMillis();
		System.out.println("耗时为：" + (end - start));

	}

	// 1. 利用通道完成文件的复制
	@Test
	public void test1() {// 11175
		long start = System.currentTimeMillis();
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			// 创建两个通道
			fis = new FileInputStream(new File("F:/BaiduYunDownload/video2.zip"));
			fos = new FileOutputStream(new File("F:/BaiduYunDownload/video2-1.zip"));
			inChannel = fis.getChannel();
			outChannel = fos.getChannel();
			// 创建一个缓冲区
			ByteBuffer buf = ByteBuffer.allocate(1024);
			// 从通道中读取数据到缓冲区
			while (inChannel.read(buf) != -1) {
				// 切换为读模式
				buf.flip();
				// 从缓冲区写入数据到输出通道
				outChannel.write(buf);
				buf.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (outChannel != null) {
				try {
					outChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inChannel != null) {
				try {
					inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("耗时为：" + (end - start));
		}
	}
}
