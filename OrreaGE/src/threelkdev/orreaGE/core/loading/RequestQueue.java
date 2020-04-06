package threelkdev.orreaGE.core.loading;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
	
	private Queue< Request > requestQueue = new LinkedList< Request >();
	
	public synchronized void addRequest( Request request ) {
		requestQueue.add( request );
	}
	
	public synchronized Request acceptNextRequest() {
		return requestQueue.poll();
	}
	
	public synchronized boolean hasRequests() {
		return !requestQueue.isEmpty();
	}
	
}
