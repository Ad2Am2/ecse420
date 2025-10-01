package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simulation of the Dining Philosophers problem with deadlock and starvation prevention.
 * Each philosopher is a thread that alternates between thinking and eating.
 * To eat, a philosopher must acquire two chopsticks (left and right).
 * This implementation avoids deadlock using resource ordering and prevents starvation 
 * using fair ReentrantLocks that guarantee FIFO ordering for waiting threads.
 */
public class DiningPhilosophers {
	
	public static void main(String[] args) {

		int numberOfPhilosophers = 5;
		Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
		ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];
		
		// Initialize chopsticks as fair ReentrantLocks
		for (int i = 0; i < numberOfPhilosophers; i++) {
			chopsticks[i] = new ReentrantLock(true); // true enables fairness
		}
		
		// Create thread pool
		ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);
		
		// Create and start philosopher threads with resource ordering
		for (int i = 0; i < numberOfPhilosophers; i++) {
			int leftChopstickId = i;
			int rightChopstickId = (i + 1) % numberOfPhilosophers;
			
			// Determine first and second chopstick based on ordering
			int firstChopstickId = Math.min(leftChopstickId, rightChopstickId);
			int secondChopstickId = Math.max(leftChopstickId, rightChopstickId);
			
			philosophers[i] = new Philosopher(i, chopsticks[firstChopstickId], 
			                                   chopsticks[secondChopstickId],
			                                   firstChopstickId, secondChopstickId);
			executor.execute(philosophers[i]);
		}
		
		// Program will run indefinitely without deadlock or starvation
	}

	public static class Philosopher implements Runnable {

		private final int id;
		private final ReentrantLock firstChopstick;
		private final ReentrantLock secondChopstick;
		private final int firstChopstickId;
		private final int secondChopstickId;
		private int mealsEaten = 0;

		public Philosopher(int id, ReentrantLock firstChopstick, ReentrantLock secondChopstick,
		                   int firstChopstickId, int secondChopstickId) {
			this.id = id;
			this.firstChopstick = firstChopstick;
			this.secondChopstick = secondChopstick;
			this.firstChopstickId = firstChopstickId;
			this.secondChopstickId = secondChopstickId;
		}

		@Override
		public void run() {
			try {
				while (true) {
					// Philosopher thinks
					think();
					
					// Philosopher tries to eat
					eat();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		private void think() throws InterruptedException {
			System.out.println("Philosopher " + id + " is thinking");
			Thread.sleep((long) (Math.random() * 100));
		}

		private void eat() throws InterruptedException {
			// Always acquire chopsticks in order (lower ID first) using fair locks
			// Fair locks guarantee FIFO ordering, preventing starvation
			firstChopstick.lock();
			try {
				System.out.println("Philosopher " + id + " picked up chopstick " + firstChopstickId);
				
				secondChopstick.lock();
				try {
					System.out.println("Philosopher " + id + " picked up chopstick " + secondChopstickId);
					System.out.println("Philosopher " + id + " is eating (meal #" + (++mealsEaten) + ")");
					Thread.sleep((long) (Math.random() * 100));
					System.out.println("Philosopher " + id + " finished eating and put down chopsticks");
				} finally {
					secondChopstick.unlock();
				}
			} finally {
				firstChopstick.unlock();
			}
		}
	}

}
