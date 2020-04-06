package threelkdev.orreage.tools.math;

public abstract class ValueDriver {
	
	private final float startTime;
	private final float length;
	
	private float currentTime = 0;
	
	private boolean firstPeriodDone = false;
	
	public ValueDriver( float length ) {
		this.length = length;
		this.startTime = 0;
	}
	
	public ValueDriver( float startTime, float length ) {
		this.length = length;
		this.startTime = startTime;
	}
	
	public float update( float delta ) {
		this.currentTime += delta;
		if( currentTime < startTime )
			return calculateValue( 0 );
		float totalTime = length + startTime;
		if( currentTime >= totalTime ) {
			currentTime %= totalTime;
			this.firstPeriodDone = true;
		}
		float relativeTime = ( currentTime - startTime ) / length;
		return calculateValue( relativeTime );
	}
	
	/**
	 * @param time - A value between 0 and 1, indicating how far into this period.
	 * @return The value at this time.
	 */
	protected abstract float calculateValue( float time );
	
	public boolean hasCompletedOnePeriod() { return firstPeriodDone; }
	
}
