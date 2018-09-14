package com.gspit.juc.alternate;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替 打印 A、B、C三个线程的ID，打印十遍
 * 
 * @author gsprinkle
 *
 *         2018年9月14日-上午8:57:06
 */
public class TestABCAlternate {

	public static void main(String[] args) {
		Alternate alternate = new Alternate();
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					alternate.loopA();
				}
			}
		}, "A").start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					alternate.loopB();
				}
			}
		}, "B").start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					alternate.loopC();
					System.out.println("--------------------------------");
				}
			}
		}, "C").start();
	}
}

class Alternate {
	// 标记 哪个线程执行
	private int number = 1;
	// 锁
	private Lock lock = new ReentrantLock();
	// 需要控制三个线程，创建三个Condition
	Condition condition1 = lock.newCondition();
	Condition condition2 = lock.newCondition();
	Condition condition3 = lock.newCondition();

	public void loopA() {
		lock.lock();
		try {
			if (number != 1) {
				condition1.await();
			}
			System.out.println(Thread.currentThread().getName());
			// 唤醒B线程
			number = 2;
			condition2.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void loopB() {
		lock.lock();
		try {
			if (number != 2) {
				condition2.await();
			}
			System.out.println(Thread.currentThread().getName());
			// 唤醒C线程
			number = 3;
			condition3.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void loopC() {
		lock.lock();
		try {
			if (number != 3) {
				condition3.await();
			}
			System.out.println(Thread.currentThread().getName());
			// 唤醒A线程
			number = 1;
			condition1.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}