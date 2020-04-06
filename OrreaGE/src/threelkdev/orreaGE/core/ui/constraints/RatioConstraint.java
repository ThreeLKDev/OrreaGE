package threelkdev.orreaGE.core.ui.constraints;

public class RatioConstraint extends UiConstraint {
	
	private UiConstraint otherConstraint;
	private final float aspectRatio;
	
	public RatioConstraint( float aspect ) {
		this.aspectRatio = aspect;
	}
	
	@Override
	protected void completeSetUp(UiConstraints other) {
		if( isPosValue() )
			this.otherConstraint = isXAxis() ? other.getY() : other.getX();
		else
			this.otherConstraint = isXAxis() ? other.getHeight() : other.getWidth();
	}

	@Override
	public float getRelativeValue() {
		float otherRelValue = otherConstraint.getRelativeValue();
		float relValue = isXAxis() ? getParent().getRelativeWidthCoords( otherRelValue )
				: getParent().getRelativeHeightCoords( otherRelValue );
		return relValue * aspectRatio;
	}

	@Override
	public void setPixelValue( int pixels ) {
		System.err.println( "Attempted to set a pixel position value for an aspect ratio controlled UI Constraint" );
	}

	@Override
	public void setRelativeValue(float value) {
		System.err.println( "Attempted to set a relative position value for an aspect ratio controlled UI Constraint" );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		//FIXME
	}
	
	@Override
	public void offsetRelative( float value ) {
		//FIXME
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		//FIXME
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		//FIXME
	}

}
