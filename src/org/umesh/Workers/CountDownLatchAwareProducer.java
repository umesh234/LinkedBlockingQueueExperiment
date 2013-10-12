package org.umesh.Workers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class CountDownLatchAwareProducer extends SimpleProducer {
	public CountDownLatchAwareProducer(int id, int packetsToProduce,
			LinkedBlockingQueue<Integer> q1, CountDownLatch startLatch) {
		super(id, packetsToProduce, q1);
		this.startLatch = startLatch;
	}

	public CountDownLatchAwareProducer(int id, int packetsToProduce,
			LinkedBlockingQueue<Integer> q1) {
		super(id, packetsToProduce, q1);
		// TODO Auto-generated constructor stub
	}

	CountDownLatch startLatch;
	
	@Override
	public void run() {
		try {
			startLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
	}
}
