package threelkdev.orreaGE.tools.math.valueDrivers;

import threelkdev.orreaGE.tools.math.Maths;
import threelkdev.orreaGE.tools.math.ValueDriver;

public class SlideDriver extends ValueDriver {
	
	private final float startValue;
	private final float endValue;
	
	public SlideDriver( float start, float end, float length ) {
		super( length );
		this.startValue = start;
		this.endValue = end;
	}
	
	public SlideDriver( float start, float end, float length, float timeDelay ) {
		super( timeDelay, length );
		this.startValue = start;
		this.endValue = end;
	}
	
	@Override
	protected float calculateValue(float time) {
		if( super.hasCompletedOnePeriod() )
			return endValue;
		return Maths.cosInterpolate( startValue, endValue, time );
	}

}
