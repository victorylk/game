package t;

import java.util.concurrent.TimeUnit;

public class Tets {
	
	private static volatile boolean stop;
	
	public static void main(String[] args) throws InterruptedException {
		
		Thread workThread= new Thread(new Runnable() {			
			@Override
			public void run() {
				int i = 0;
				while (!stop) {
					i++;
					System.out.println("i£º"+i);
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});	
		workThread.start();
		TimeUnit.SECONDS.sleep(3);
		stop = true;
	}
}
