package threelkdev.orreaGE.core.loading;

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
