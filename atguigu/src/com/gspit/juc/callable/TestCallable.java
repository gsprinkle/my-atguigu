package com.gspit.juc.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestCallable {
	
	public static void main(String[] args) {
		SalTicket ticket = new SalTicket();
		
		FutureTask<Integer> futureTask = new FutureTask<>(ticket);
		new Thread(futureTask).start();
		Integer result = 0;
		try {
			result = futureTask.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("---------------------------------------------");
		System.out.println(result);
	}
}

class SalTicket implements Callable<Integer>{

	@Override
	public Integer call() throws Exception {
		int sum =0;
		for(int i=1;i<Integer.MAX_VALUE;i++){
			sum += i;
		}
		return sum;
	}
	
}