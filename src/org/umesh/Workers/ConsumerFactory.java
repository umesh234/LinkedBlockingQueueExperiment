package org.umesh.Workers;

import java.util.concurrent.LinkedBlockingQueue;

public class ConsumerFactory {

	
	public static Consumer createConsumer( int id , int unitsOfWork, LinkedBlockingQueue<Integer> q) {
		return new SimpleConsumer ( id, unitsOfWork, q);
	}
}
