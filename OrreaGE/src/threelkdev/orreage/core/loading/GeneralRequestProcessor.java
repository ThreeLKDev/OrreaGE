package threelkdev.orreage.core.loading;

import threelkdev.orreage.core.loading.Request;
import threelkdev.orreage.core.loading.RequestQueue;

public class GeneralRequestProcessor extends Thread {
	
	private RequestQueue requestQueue = new RequestQueue();
	private boolean running = true;
	
	protected GeneralRequestProcessor() {
		this.start();
	}
	
	public synchronized void run() {
		while( running || requestQueue.hasRequests() ) {
			if( requestQueue.hasRequests() ) {
				requestQueue.acceptNextRequest().execute();
			} else {
				try {
					wait();
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void sendRequest( Request request ) {
		boolean isPaused = !requestQueue.hasRequests();
		requestQueue.addRequest( request );
		if( isPaused ) {
			indicateNewRequests();
		}
	}
	
	protected void kill() {
		running = false;
		indicateNewRequests();
	}
	
	private synchronized void indicateNewRequests() {
		notify();
	}
	
}
