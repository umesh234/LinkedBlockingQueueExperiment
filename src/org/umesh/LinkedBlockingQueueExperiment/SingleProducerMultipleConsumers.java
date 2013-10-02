package org.umesh.LinkedBlockingQueueExperiment;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;



public class SingleProducerMultipleConsumers{

	static LinkedBlockingQueue<Integer> q1 = new LinkedBlockingQueue<Integer>();
	static int MAX = 1000000;
	
	public static void main(String args[]) throws InterruptedException {
		
		int trials =5;
		for( int THREAD_COUNT =5 ; THREAD_COUNT < 100 ; THREAD_COUNT += 10) {
			long duration =  0 ;
			for( int i=0;i<trials ;i++) {
				int pmsgs = MAX;
						duration +=runTest(THREAD_COUNT , pmsgs, pmsgs/THREAD_COUNT);
			}
			System.out.println(" ThreadCount:" + THREAD_COUNT + "  Duration is " + duration/trials);
		}
	}
	
	public static long runTest( int THREAD_COUNT, int pmsgs, int cmsgs  ) throws InterruptedException {
		long begin = System.currentTimeMillis();
		//System.out.println(" Creating threads Time: " + begin);
		Vector<Thread> threads = new Vector<Thread>();
		for ( int i =0; i < THREAD_COUNT ; i++) {
			Thread t2 = new Thread ( new Consumer(cmsgs));
			threads.add(t2);
		}
		Thread t1 = new Thread ( new Producer(pmsgs));
		threads.add(t1);
	
		
		for  ( int i=0;i< threads.size(); i++ )
			threads.get(i).start();
		
		//System.out.println(" Starting threads Time: " + begin);
		for  ( int i=0;i< threads.size(); i++ )
			threads.get(i).join();
		
		long end = System.currentTimeMillis();
		System.out.println(" End Time: " + end + " Duration : " + (end - begin ) + " to consume: " + MAX + " Threads: " + THREAD_COUNT);
		return ( end- begin);
		
	}
	public static class Producer implements Runnable {

		int pmsgs = 0;
		public Producer( int pmsgs) {
			this.pmsgs = pmsgs;
		}
		@Override
		public void run() {
			int i = 0 ;
			while ( i < pmsgs) {
				q1.add(i);
				i++;
				if( i % (pmsgs/2) == 0  ) {
					System.out.print(" Produced : " + i);
				}
			}
		}

	}

	public static class Consumer implements Runnable {

		int pmsgs = 0;
		public Consumer( int pmsgs) {
			this.pmsgs = pmsgs;
		}
		
		@Override
		public void run() {
			int i=0;
			while ( i < pmsgs) {
				try {
					q1.take();
					i++;
					if( i % (pmsgs/2) == 0  ) {
						System.out.print(" Consumed : " + i);
					}
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
}
