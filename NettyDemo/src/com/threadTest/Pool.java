package com.threadTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pool {
	public static void main(String[] args) {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		pool.execute(new Run1());
		
	}
}

class Run1 implements Runnable{

	@Override
	public void run() {
		System.out.println("threadpool");		
	}
	
}
