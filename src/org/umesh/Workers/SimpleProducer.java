package org.umesh.Workers;

import java.util.concurrent.LinkedBlockingQueue;

public class SimpleProducer implements Producer {

	public SimpleProducer(int id , int packetsToProduce, LinkedBlockingQueue<Integer> q1) {
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
		System.out.println(" ProducerThread : " + id + " started");
		int i = 0 ;
		while ( i < packetsToProduce) {
			q1.add(i);
			i++;
			if( i % (packetsToProduce/2) == 0  ) {
				System.out.print(" ProducerThread : " + id + " produced : " + i);
			}
		}
		System.out.println(" ProducerThread : " + id + " completed");
	}

}