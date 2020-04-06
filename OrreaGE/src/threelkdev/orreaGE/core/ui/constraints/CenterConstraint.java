package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.tools.math.Maths;

public class CenterConstraint extends UiConstraint {
	
	private UiConstraint sizeConstraint;
	private float offset = 0;
	
	public CenterConstraint() { }
	
	@Override
	protected void completeSetUp(UiConstraints other) {
		// TODO check pos value
		this.sizeConstraint = isXAxis() ? other.getWidth() : other.getHeight();
	}

	@Override
	public float getRelativeValue() {
		float relSize = sizeConstraint.getRelativeValue();
		return ( ( 1f - relSize ) / 2f ) + offset;
	}

	@Override
	public void setPixelValue(int pixels) {
		this.offset = pixels / getParentPixelSize();
		notifyDimensionChange( false );
	}

	@Override
	public void setRelativeValue(float value) {
		this.offset = getParentPixelSize();
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		if( pixels == 0 ) return;
		this.offset += pixels / getParentPixelSize();
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelative( float value ) {
		if( value == 0 ) return;
		this.offset += value;
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		if( pixels == 0 ) return;
		this.offset = Maths.clamp( this.offset + ( pixels / getParentPixelSize() ), min / getParentPixelSize(), max / getParentPixelSize() );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		if( value == 0 ) return;
		this.offset = Maths.clamp( this.offset + value, min, max );
		notifyDimensionChange( false );
	}

}
