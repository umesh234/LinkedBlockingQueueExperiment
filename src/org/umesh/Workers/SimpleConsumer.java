package org.umesh.Workers;

import java.util.concurrent.LinkedBlockingQueue;
public class SimpleConsumer implements Runnable {
	public SimpleConsumer(int id , int packetsToProduce, LinkedBlockingQueue<Integer> q1) {
		super();
		this.packetsToProduce = packetsToProduce;
		this.id = id;
		this.q1 = q1;
	}

	int packetsToProduce ;
	LinkedBlockingQueue<Integer> q1;
	int id;
	@Override
	public void run() {
		System.out.println(" ConsumerThread : " + id + " started");
		int i=0;
		while ( i < packetsToProduce) {
			try {
				q1.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
			if( i % (packetsToProduce/2) == 0  ) {
				System.out.print(" ConsumerThread : " + id + " produced : " + i);
			}
		}
		System.out.println(" ConsumerThread : " + id + " completed");
	}
}