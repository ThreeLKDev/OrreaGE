package threelkdev.orreaGE.core.ui.animation;

import threelkdev.orreaGE.core.ui.animation.transitions.ValueTransition;
import threelkdev.orreaGE.tools.math.ValueDriver;

public class Animation {
	
	private ValueTransition xDriver, yDriver,
	widthDriver, heightDriver, alphaDriver;
	
	public Animation( ValueTransition xDriver, ValueTransition yDriver, ValueTransition widthDriver, 
			ValueTransition heightDriver, ValueTransition alphaDriver ) {
		this.xDriver = xDriver;
		this.yDriver = yDriver;
		this.widthDriver = widthDriver;
		this.heightDriver = heightDriver;
		this.alphaDriver = alphaDriver;
	}
	
	public Animation() {}
	
	public Animation xDriver( ValueTransition driver ) {
		this.xDriver = driver;
		return this;
	}
	
	public Animation yDriver( ValueTransition driver ) {
		this.yDriver = driver;
		return this;
	}
	
	public Animation widthDriver( ValueTransition driver ) {
		this.widthDriver = driver;
		return this;
	}
	
	public Animation heightDriver( ValueTransition driver ) {
		this.heightDriver = driver;
		return this;
	}
	
	public Animation alphaDriver( ValueTransition driver ) {
		this.alphaDriver = driver;
		return this;
	}
	
	public ValueTransition getXDriver() { return xDriver; }
	public ValueTransition getYDriver() { return yDriver; }
	public ValueTransition getWidthDriver() { return widthDriver; }
	public ValueTransition getHeightDriver() { return heightDriver; }
	public ValueTransition getAlphaDriver() { return alphaDriver; }
	
	protected ValueDriver initXDriver( boolean display, float currentValue, float delay ) {
		return xDriver == null ? null : xDriver.initDriver( currentValue, display, delay );
	}
	
	protected ValueDriver initYDriver( boolean display, float currentValue, float delay ) {
		return yDriver == null ? null : yDriver.initDriver( currentValue, display, delay );
	}
	
	protected ValueDriver initWidthDriver( boolean display, float currentValue, float delay ) {
		return widthDriver == null ? null : widthDriver.initDriver( currentValue, display, delay );
	}
	
	protected ValueDriver initHeightDriver( boolean display, float currentValue, float delay ) {
		return heightDriver == null ? null : heightDriver.initDriver( currentValue, display, delay );
	}
	
	protected ValueDriver initAlphaDriver( boolean display, float currentValue, float delay ) {
		return alphaDriver == null ? null : alphaDriver.initDriver( currentValue, display, delay );
	}
	
}
