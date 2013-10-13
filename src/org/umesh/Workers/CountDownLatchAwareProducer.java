package org.umesh.Workers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class CountDownLatchAwareProducer extends SimpleProducer {
	public CountDownLatchAwareProducer(int id, int packetsToProduce,
			LinkedBlockingQueue<Integer> q1, CountDownLatch startLatch, CountDownLatch producerFinishedSignal) {
		super(id, packetsToProduce, q1);
		this.startLatch = startLatch;
		this.finishLatch = producerFinishedSignal;
	}
	CountDownLatch startLatch;
	CountDownLatch finishLatch;
	
	@Override
	public void run() {
		try {
			startLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
		finishLatch.countDown();
	}
}
