package threelkdev.orreaGE.core.ui.animation.transitions;

import threelkdev.orreaGE.core.ui.animation.transitions.ValueTransition;
import threelkdev.orreaGE.tools.math.ValueDriver;
import threelkdev.orreaGE.tools.math.valueDrivers.SlideDriver;

public class SlideTransition implements ValueTransition {
	
	private final float normalValue;
	private final float offsetValue;
	private final float duration;
	
	public SlideTransition( float normal, float offset, float duration ) {
		this.normalValue = normal;
		this.offsetValue = offset;
		this.duration = duration;
	}
	
	@Override
	public float getHiddenValue() {
		return offsetValue;
	}

	@Override
	public ValueDriver initDriver(float currentValue, boolean display, float delay) {
		float target = display ? normalValue : offsetValue;
		return new SlideDriver( currentValue, target, duration, delay );
	}

}
