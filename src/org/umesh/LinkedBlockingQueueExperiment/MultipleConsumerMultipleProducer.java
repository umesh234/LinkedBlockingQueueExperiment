package org.umesh.LinkedBlockingQueueExperiment ;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.umesh.Workers.CountDownLatchAwareConsumer;
import org.umesh.Workers.CountDownLatchAwareProducer;
import org.umesh.Workers.Producer;
import org.umesh.Workers.ProducerFactory;
import org.umesh.Workers.SimpleConsumer;
import org.umesh.Workers.SimpleProducer;



public class MultipleConsumerMultipleProducer {

	static LinkedBlockingQueue<Integer> q1 = new LinkedBlockingQueue<Integer>();
	
	public static void main(String args[]) throws InterruptedException, ExecutionException {
		
		double totalProducerPackets = 10000000;
		double totalConsumerPackets = 10000000;
		
		int producerThreads = 10;
		int consumerThreads = 10;
		int producerThreadPool = 1;
		int consumerThreadPool =1;
		
		int produce = (int) (totalProducerPackets/producerThreads);
		int consume = (int) (totalConsumerPackets/consumerThreads);
		
		ProducerFactory simpleP = ProducerFactory.createSimpleProducerFactory();
		
		//runThreadPoolTestSimpleApproach(producerThreads, consumerThreads, producerThreadPool, consumerThreadPool, produce, consume);
		runThreadPoolTestSimple2Approach(producerThreads, consumerThreads, producerThreadPool, consumerThreadPool, produce, consume);
	}
	
	//all threads start at same time
	//using latch to finish waiting on producers and consumers to finish also
	public static void runThreadPoolTestSimple2Approach( int producerCount, int consumerCount, int producerThreadPoolSize, int consumerThreadPoolSize, int produce, int consume  ) throws InterruptedException {
		ExecutorService producersES = Executors.newFixedThreadPool(producerThreadPoolSize);
		ExecutorService consumersES = Executors.newFixedThreadPool(consumerThreadPoolSize);
		ArrayList<Future> runningThreadsTrackers = new ArrayList<Future>(); //what type parameter should i pass in to future here ?
		
		CountDownLatch producerStartSignal = new CountDownLatch(1);
		CountDownLatch consumerStartSignal = new CountDownLatch(1);
		CountDownLatch producerFinishedSignal = new CountDownLatch(producerCount);
		CountDownLatch consumerFinishedSignal = new CountDownLatch(consumerCount);
		
		for ( int i =0; i < producerCount ; i++) {
			CountDownLatchAwareProducer t1 = new CountDownLatchAwareProducer(i,produce,q1, producerStartSignal,producerFinishedSignal);
			Future f = producersES.submit(t1); //better exception eating than execute
			runningThreadsTrackers.add(f);
		}
		
		for ( int i =0; i < consumerCount ; i++) {
			CountDownLatchAwareConsumer t2 =new CountDownLatchAwareConsumer(i, consume,q1, consumerStartSignal,consumerFinishedSignal);
			Future f = consumersES.submit(t2);
			runningThreadsTrackers.add(f);
		}
		
		System.out.println("Starting all threads now");
		producerStartSignal.countDown();
		consumerStartSignal.countDown();
		
		producerFinishedSignal.await();
		consumerFinishedSignal.await();
		System.out.println(" All threads have finished");
	}
	
	//run threads thru pool's..threads start at different timings...crude waiting for all to finish
	public static void runThreadPoolTestSimpleApproach( int producerCount, int consumerCount, int producerThreadPoolSize, int consumerThreadPoolSize, int produce, int consume ,ProducerFactory p ) throws InterruptedException, ExecutionException {
		ExecutorService producersES = Executors.newFixedThreadPool(producerThreadPoolSize);
		ExecutorService consumersES = Executors.newFixedThreadPool(consumerThreadPoolSize);
		ArrayList<Future> runningThreadsTrackers = new ArrayList<Future>(); //what type parameter should i pass in to future here ?
		
		for ( int i =0; i < producerCount ; i++) {
			Producer t1 = p.createProducer(i,produce,q1);
			Future f = producersES.submit(t1); //better exception eating than execute
			runningThreadsTrackers.add(f);
		}
		
		for ( int i =0; i < consumerCount ; i++) {
			SimpleConsumer t2 =new SimpleConsumer(i, consume,q1);
			Future f = consumersES.submit(t2);
			runningThreadsTrackers.add(f);
		}
		
		//wait for futures to finish
		for ( Future f : runningThreadsTrackers) {
			f.get();
		}
		System.out.println(" All threads have finished");
		
	}
		
	
	//create individual threads
	public static long runTest( int THREAD_COUNT, int produce, int consume  ) throws InterruptedException {
		long begin = System.currentTimeMillis();
		//System.out.println(" Creating threads Time: " + begin);
		Vector<Thread> threads = new Vector<Thread>();
		for ( int i =0; i < THREAD_COUNT ; i++) {
			Thread t1 = new Thread ( new SimpleProducer(i,produce,q1));
			Thread t2 = new Thread ( new SimpleConsumer(i, consume,q1));
			threads.add(t1);
			threads.add(t2);
		}
		
		for  ( int i=0;i< threads.size(); i++ )
			threads.get(i).start();
		
		//System.out.println(" Starting threads Time: " + begin);
		for  ( int i=0;i< threads.size(); i++ )
			threads.get(i).join();
		
		long end = System.currentTimeMillis();
		System.out.println(" End Time: " + end + " Duration : " + (end - begin ) + " to consume: " + produce*THREAD_COUNT + " Threads: " + THREAD_COUNT);
		return ( end- begin);
		
	}
}
