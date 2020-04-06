package threelkdev.orreage.core.loading;

public class BackgroundLoader {
	
	private static GlRequestProcessor glRequestProcessor;
	private static GeneralRequestProcessor generalRequestProcessor;
	private static long mainThreadId;
	
	public static void init() {
		glRequestProcessor = new GlRequestProcessor();
		generalRequestProcessor = new GeneralRequestProcessor();
		mainThreadId = Thread.currentThread().getId();
	}
	
	public static void update() {
		glRequestProcessor.dealWithTopRequests();
	}
	
	public static void addGeneralRequest( Request request ) {
		generalRequestProcessor.sendRequest( request );
	}
	
	public static void addOpenGlRequest( Request request ) {
		if ( Thread.currentThread().getId() == mainThreadId ) {
			request.execute();
		} else {
			glRequestProcessor.sendRequest( request );
		}
	}
	
	public static void cleanUp() {
		glRequestProcessor.completeAllRequests();
		generalRequestProcessor.kill();
	}
}
