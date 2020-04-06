package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.core.ui.UiComponent;

public abstract class UiConstraint {
	
	private boolean xAxis;
	private boolean posValue;
	private UiComponent parent, current;
	
	protected void setAxis( boolean xAxis, boolean posValue ) {
		this.xAxis = xAxis;
		this.posValue = posValue;
	}
	
	protected boolean isXAxis() { return xAxis; }
	protected boolean isPosValue() { return posValue; }
	protected UiComponent getParent() { return parent; }
	protected float getParentPixelSize() {
		return xAxis ? parent.getPixelWidth() : parent.getPixelHeight();
	}
	
	protected void notifyDimensionChange( boolean scaleChange ) {
		current.notifyDimensionChange( scaleChange );
	}
	
	protected void notifyAdded( UiConstraints other, UiComponent current, UiComponent parent ) {
		this.parent = parent;
		this.current = current;
		completeSetUp( other );
	}
	
	protected abstract void completeSetUp( UiConstraints other );
	
	public abstract float getRelativeValue();
	public abstract void setPixelValue( int pixels );
	public abstract void setRelativeValue( float value );
	public abstract void offsetPixel( int pixels );
	public abstract void offsetRelative( float value );
	public abstract void offsetPixelClamp( int pixels, int min, int max );
	public abstract void offsetRelativeClamp( float value, float min, float max );
	
	//TODO getters?
}
