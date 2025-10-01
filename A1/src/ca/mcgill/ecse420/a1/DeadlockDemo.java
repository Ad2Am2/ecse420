package ca.mcgill.ecse420.a1;

/**
 * A simple program that demonstrates deadlock using two threads and two resources.
 * Thread 1 acquires Resource A then tries to acquire Resource B.
 * Thread 2 acquires Resource B then tries to acquire Resource A.
 * This creates a circular wait condition leading to deadlock.
 */
public class DeadlockDemo {
	
	// Two shared resources that threads will compete for
	private static final Object resourceA = new Object();
	private static final Object resourceB = new Object();
	
	public static void main(String[] args) {
		System.out.println("Starting deadlock demonstration...");
		System.out.println("This program will hang indefinitely due to deadlock.\n");
		
		// Create first thread that locks A then B
		Thread thread1 = new Thread(() -> {
			synchronized (resourceA) {
				System.out.println("Thread 1: Acquired lock on Resource A");
				
				// Small delay to ensure both threads acquire their first lock
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Thread 1: Waiting for lock on Resource B...");
				synchronized (resourceB) {
					System.out.println("Thread 1: Acquired lock on Resource B");
				}
			}
		});
		
		// Create second thread that locks B then A
		Thread thread2 = new Thread(() -> {
			synchronized (resourceB) {
				System.out.println("Thread 2: Acquired lock on Resource B");
				
				// Small delay to ensure both threads acquire their first lock
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Thread 2: Waiting for lock on Resource A...");
				synchronized (resourceA) {
					System.out.println("Thread 2: Acquired lock on Resource A");
				}
			}
		});
		
		// Start both threads
		thread1.start();
		thread2.start();
		
		// Wait for threads to complete (they never will due to deadlock)
		try {
			thread1.join();
			thread2.join();
			System.out.println("Both threads completed successfully.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
