package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.tools.math.Maths;

public class RelativeConstraint extends UiConstraint {
	
	private float relativeValue;
	
	public RelativeConstraint( float value ) {
		this.relativeValue = value;
	}
	
	@Override
	protected void completeSetUp(UiConstraints other) { }

	@Override
	public float getRelativeValue() {
		return relativeValue;
	}

	@Override
	public void setPixelValue(int pixels) {
		this.relativeValue = pixels / super.getParentPixelSize();
		notifyDimensionChange( !super.isPosValue() );
	}

	@Override
	public void setRelativeValue(float value) {
		if( relativeValue == value )
			return;
		this.relativeValue = value;
		notifyDimensionChange( !super.isPosValue() );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		if( pixels == 0 ) return;
		this.relativeValue += pixels / getParentPixelSize();
		notifyDimensionChange( !isPosValue() );
	}
	
	@Override
	public void offsetRelative( float value ) {
		if( value == 0 ) return;
		this.relativeValue += value;
		notifyDimensionChange( !isPosValue() );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		if( pixels == 0 ) return;
		this.relativeValue = Maths.clamp( this.relativeValue + ( pixels / getParentPixelSize() ), min / getParentPixelSize(), max / getParentPixelSize() );
		notifyDimensionChange( !isPosValue() );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		if( value == 0 ) return;
		this.relativeValue = Maths.clamp( this.relativeValue + value, min, max );
		notifyDimensionChange( !isPosValue() );
	}

}
