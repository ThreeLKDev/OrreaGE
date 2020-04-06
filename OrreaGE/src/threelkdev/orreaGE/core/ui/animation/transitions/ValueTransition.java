package threelkdev.orreaGE.core.ui.animation.transitions;

import threelkdev.orreaGE.tools.math.ValueDriver;

public interface ValueTransition {
	
	public float getHiddenValue();
	public ValueDriver initDriver( float currentValue, boolean display, float delay );
	
}
