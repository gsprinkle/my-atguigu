/*package com.gspit.juc.wait_notify;

public class TestProductAndConsumer {
	
	public static void main(String[] args) {
		Clerk clerk = new Clerk();
		Productor productor = new Productor(clerk);
		Consumer consumer = new Consumer(clerk);
		
		new Thread(productor,"生产者A").start();
		new Thread(consumer,"消费者B").start();
		new Thread(productor,"生产者C").start();
		new Thread(consumer,"消费者D").start();
	}
}

*//**
 * 店员
 * @author gsprinkle
 *
 * 2018年9月13日-下午4:35:21
 *//*
class Clerk{
	// 库存
	int product;
	// 生产产品
	public synchronized void get(){
		while(product >=1){
			System.out.println("库存已满！");
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.currentThread().sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "生产了产品：" + ++ product);
		notifyAll();
	}
	// 消费产品
	public synchronized void sale(){
		while(product < 1){
			System.out.println("已售罄！");
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + "消费了产品：" + --product);
		notifyAll();
	}
	
}

*//**
 * 生产者
 * @author gsprinkle
 *
 * 2018年9月13日-下午4:35:08
 *//*
class Productor implements Runnable{
	private Clerk clerk;
	public Productor(Clerk clerk) {
		this.clerk = clerk;
	}
	@Override
	public void run() {
		for(int i =0;i<20;i++){
			clerk.get();
		}
	}
	
}

*//**
 * 消费者
 * @author gsprinkle
 *
 * 2018年9月13日-下午4:35:32
 *//*
class Consumer implements Runnable{
	private Clerk clerk;
	public Consumer(Clerk clerk) {
		this.clerk = clerk;
	}
	@Override
	public void run() {
		for(int i =0;i<20;i++){
			clerk.sale();;
		}
	}
	
}*/