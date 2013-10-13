package org.umesh.Workers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerFactory {

	public ProducerFactory(CountDownLatch startLatch,
			CountDownLatch finishedLatch) {
		super();
		this.startLatch = startLatch;
		this.finishedLatch = finishedLatch;
	}

	private final CountDownLatch startLatch ;
	private final CountDownLatch finishedLatch ;
	
	public  Producer createProducer( int id , int unitsOfWork, LinkedBlockingQueue<Integer> q) {
		if ( startLatch != null && finishedLatch != null )
			return new CountDownLatchAwareProducer(id,  unitsOfWork, q, startLatch, finishedLatch) ;
		else
			return new SimpleProducer( id, unitsOfWork, q);
	}
	
	public static ProducerFactory createSimpleProducerFactory() {
		return new ProducerFactory(null,null);
	}
	
	//how do i work around the need to pass in count down latch here ?
	//is this the right way to use factory ?
	public static ProducerFactory createLatchProducerFactory(CountDownLatch startLatch ,CountDownLatch finishLatch) {
		return new ProducerFactory ( startLatch, finishLatch);
	}
}
