package com.gspit.juc.wait_notify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestProductAndConsumerLock {

	public static void main(String[] args) {
		Clerk clerk = new Clerk();
		Productor productor = new Productor(clerk);
		Consumer consumer = new Consumer(clerk);

		new Thread(productor, "生产者A").start();
		new Thread(consumer, "消费者B").start();
		new Thread(productor, "生产者C").start();
		new Thread(consumer, "消费者D").start();
	}
}

/**
 * 店员
 * 
 * @author gsprinkle
 *
 *         2018年9月13日-下午4:35:21
 */
class Clerk {
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	// 库存
	int product;

	// 生产产品
	public void get() {
		lock.lock();
		try {
			if (product >= 1) {
				System.out.println("库存已满！");
				condition.await();
			}
			System.out.println(Thread.currentThread().getName() + "生产了产品：" + ++product);
			condition.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	// 消费产品
	public void sale() {
		lock.lock();
		try {
			if (product < 1) {
				System.out.println("已售罄！");
				condition.await();
			}
			System.out.println(Thread.currentThread().getName() + "消费了产品：" + --product);
			condition.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}

/**
 * 生产者
 * 
 * @author gsprinkle
 *
 *         2018年9月13日-下午4:35:08
 */
class Productor implements Runnable {
	private Clerk clerk;

	public Productor(Clerk clerk) {
		this.clerk = clerk;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.get();
		}
	}

}

/**
 * 消费者
 * 
 * @author gsprinkle
 *
 *         2018年9月13日-下午4:35:32
 */
class Consumer implements Runnable {
	private Clerk clerk;

	public Consumer(Clerk clerk) {
		this.clerk = clerk;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.sale();
			;
		}
	}

}