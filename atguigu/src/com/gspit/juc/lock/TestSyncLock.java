package com.gspit.juc.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestSyncLock {

	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		TicketRunnable tr = new TicketRunnable(lock);
		for (int i = 0; i < 3; i++) {
			new Thread(tr).start();
		}
	}
}

class TicketRunnable implements Runnable {
	Lock lock;

	public TicketRunnable(Lock lock) {
		this.lock = lock;
	}

	int ticket = 100;

	@Override
	public void run() {
		while (true) {
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lock.lock();
			try {
				if (ticket > 0) {
					System.out.println(Thread.currentThread().getName() + "卖出了第" + ticket-- + "张票");
				} else {
					break;
				}
			} finally {
				lock.unlock();
			}
		}
	}

}
