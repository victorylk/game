package com.threadTest;

/**
 * ¿ÉÖØÈëËø
 * @author like
 */
public class LockRettaent implements Runnable {
	
	public synchronized void get(){
		System.out.println(Thread.currentThread().getId());
		set();
	}
	public synchronized void set(){
		System.out.println(Thread.currentThread().getId());		
	}
	@Override
	public void run() {
		get();
	}
	
	public static void main(String[] args) {
		LockRettaent test = new LockRettaent();
		new Thread(test).start();;
		new Thread(test).start();;
		new Thread(test).start();;
	}	
}
