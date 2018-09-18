package com.gspit.juc.read_write_lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁 ReadWriteLock 写 写操作/读 写操作 这两种操作之间上锁，而读、读操作之间不需要上锁
 * 
 * @author gsprinkle
 *
 *         2018年9月14日-上午10:18:23
 */
public class TestReadWriteLock {

	public static void main(String[] args) {
		ReadWriteLockDemo rw = new ReadWriteLockDemo();
		// 创建一个写线程，写数据
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				rw.setNumber((int)(Math.random() * 101));
			}
		},"Write线程").start();
		// 循环创建 100 个读线程，同时读取数据
		for(int i=0;i<100;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					rw.readNumber();
				}
			}).start();
		}
	}
}

class ReadWriteLockDemo {
	// 定义一个读写锁
	ReadWriteLock rwLock = new ReentrantReadWriteLock();
	// 共享 数据
	private int number;

	// 读 操作使用读锁
	public void readNumber() {
		rwLock.readLock().lock();
		try{
			System.out.println(Thread.currentThread().getName() + ":" + number);
		}finally{
			rwLock.readLock().unlock();
		}
	}

	// 写 操作使用 写 锁
	public void setNumber(int num) {
		rwLock.writeLock().lock();
		try {
			number = num;
			System.out.println(Thread.currentThread().getName() + " write in :" + num);

		} finally {
			rwLock.writeLock().unlock();
		}
	}
}
