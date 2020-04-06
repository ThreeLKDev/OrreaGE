package threelkdev.orreage.core.loading;

import threelkdev.orreage.core.loading.Request;
import threelkdev.orreage.core.loading.RequestQueue;

public class GlRequestProcessor {
	
	private static final float MAX_TIME_MILLIS = 5f;
	
	private RequestQueue requestQueue = new RequestQueue();
	
	protected void sendRequest( Request request ) {
		requestQueue.addRequest( request );
	}
	
	protected void dealWithTopRequests() {
		if( !requestQueue.hasRequests() ) {
			return;
		}
		float remainingTime = MAX_TIME_MILLIS * 1000000;
		long start = System.nanoTime();
		while( requestQueue.hasRequests() ) {
			requestQueue.acceptNextRequest().execute();
			long end = System.nanoTime();
			long timeTaken = end - start;
			remainingTime -= timeTaken;
			start = end;
			if( remainingTime < 0 ) {
				break;
			}
		}
	}
	
	protected void completeAllRequests() {
		while( requestQueue.hasRequests() ) {
			requestQueue.acceptNextRequest().execute();
		}
	}
	
}
