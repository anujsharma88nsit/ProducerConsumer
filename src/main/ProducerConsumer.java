package main;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<>();
		final int MAX = 3;
		new Thread(new Producer(queue, MAX)).start();
		new Thread(new Producer(queue, MAX)).start();
		new Thread(new Producer(queue, MAX)).start();
		new Thread(new Producer(queue, MAX)).start();
		new Thread(new Consumer(queue)).start();
		new Thread(new Consumer(queue)).start();
		new Thread(new Consumer(queue)).start();
		new Thread(new Consumer(queue)).start();
		new Thread(new Consumer(queue)).start();
	}
}

class Producer implements Runnable {
	Queue<Integer> queue;
	private final int MAX;
	
	Producer(Queue<Integer> queue, int max) {
		this.queue = queue;
		this.MAX = max;
	}
	
	public void run() {
		synchronized(queue) {
			while(queue.size()==MAX) {
				try {
					System.out.println("Going to wait for Consumer thread to notify me when i can add something");
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int data = (int)(Math.random()*100);
			queue.offer(data);
			System.out.println("Produced: " + data);
			queue.notify();
		}
	}
}

class Consumer implements Runnable {
	Queue<Integer> queue;
	
	Consumer(Queue<Integer> queue) {
		this.queue = queue;
	}
	
	public void run() {
		synchronized(queue) {
			while(queue.size()==0) {
				try {
					System.out.println("Going to wait for Producer thread to notify me when i can eat something");
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Consumed: " + queue.poll());
			queue.notify();
		}
	}
}