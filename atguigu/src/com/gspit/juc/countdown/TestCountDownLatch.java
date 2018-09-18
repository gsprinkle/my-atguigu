package com.gspit.juc.countdown;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {

	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(5);
		CountDownTest cd = new CountDownTest(latch);
		long start = System.currentTimeMillis();
		for(int i = 0; i<5;i++){
			new Thread(cd).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("共计用时："+(end - start));
	}
}

class CountDownTest implements Runnable{
	private CountDownLatch latch;
	public CountDownTest(CountDownLatch latch){
		this.latch = latch;
	}
	@Override
	public void run() {
		for(int i =0;i<10000;i++){
			if(i %2 ==0){
				System.out.println(i);
			}
		}
		latch.countDown();
	}
	
	
}
