package threelkdev.orreaGE.tools.utils;

public class OrreaRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 7450050813791683187L;
	
	public OrreaRuntimeException( String message ) {
		super( message );
	}
	
	public OrreaRuntimeException( Throwable t ) {
		super( t );
	}
	
	public OrreaRuntimeException( String message, Throwable t ) {
		super( message, t );
	}
	
}
