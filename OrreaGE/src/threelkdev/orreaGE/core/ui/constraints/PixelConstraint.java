package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.tools.math.Maths;

public class PixelConstraint extends UiConstraint {
	
	private int value;
	private final boolean flipAxis;
	public PixelConstraint( int value ) { this( value, false ); }
	public PixelConstraint( int value, boolean flipAxis ) {
		this.value = value;
		this.flipAxis = flipAxis;
	}
	
	
	@Override
	protected void completeSetUp(UiConstraints other) { }

	@Override
	public float getRelativeValue() {
		float parentSizePixels = isXAxis() ? getParent().getPixelWidth() : getParent().getPixelHeight();
		float relValue = value / parentSizePixels;
		return flipAxis ? 1 - relValue : relValue;
	}

	@Override
	public void setPixelValue(int pixels) {
		if( value == pixels )
			return;
		this.value = pixels;
		notifyDimensionChange( !super.isPosValue() );
	}

	@Override
	public void setRelativeValue(float value) {
		value = flipAxis ? 1 - value : value;
		this.value = (int) Math.round( super.getParentPixelSize() * value );
		notifyDimensionChange( !super.isPosValue() );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		if( pixels == 0 ) return;
		this.value += pixels;
		notifyDimensionChange( !isPosValue() );
	}
	
	@Override
	public void offsetRelative( float value ) {
		if( value == 0 ) return;
		this.value += Math.round( super.getParentPixelSize() * value ); 
		notifyDimensionChange( !isPosValue() );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		if( pixels == 0 ) return;
		this.value = Maths.clamp( this.value + pixels, min, max );
		notifyDimensionChange( !isPosValue() );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		if( value == 0 ) return;
//		this.value = (int) Maths.clamp( this.value + Math.round( value * getParentPixelSize() ), Math.round( min * getParentPixelSize() ), Math.round( max * getParentPixelSize() ) );
		this.value = (int) ( Maths.clamp( this.value + value * getParentPixelSize(), min * getParentPixelSize(), max * getParentPixelSize() ) + 0.5f );
		notifyDimensionChange( !isPosValue() );
	}

}
