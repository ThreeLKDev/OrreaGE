package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.tools.math.Maths;

public class PixelOtherConstraint extends UiConstraint {
	
	private int padding;
	private UiConstraint otherConstraint;
	
	public PixelOtherConstraint( int padding ) {
		this.padding = padding;
	}
	
	@Override
	protected void completeSetUp(UiConstraints other) {
		this.otherConstraint = isXAxis() ? other.getWidth() : other.getHeight();
	}

	@Override
	public float getRelativeValue() {
		float size = otherConstraint.getRelativeValue();
		float pad = isXAxis() ? getParent().pixelsToRelativeX( padding )
				: getParent().pixelsToRelativeY( padding );
		return 1f - ( size + pad );
	}

	@Override
	public void setPixelValue(int pixels) {
		if( padding == pixels )
			return;
		this.padding = pixels;
		notifyDimensionChange( false );
	}

	@Override
	public void setRelativeValue(float value) {
		this.padding = (int) Math.round( super.getParentPixelSize() * value );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		if( pixels == 0 ) return;
		this.padding += pixels;
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelative( float value ) {
		if( value == 0 ) return;
		this.padding += Math.round( getParentPixelSize() * value );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		if( pixels == 0 ) return;
		this.padding = Maths.clamp( this.padding + pixels, min, max);
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		if( value == 0 ) return;
//		this.padding = (int) Maths.clamp( this.padding + Math.floor( getParentPixelSize() * value ), Math.floor( getParentPixelSize() * min ), Math.floor( getParentPixelSize() * max ) );
		this.padding = (int) ( Maths.clamp( this.padding + getParentPixelSize() * value, getParentPixelSize() * min , getParentPixelSize() * max ) + 0.5f );
		notifyDimensionChange( false );
	}

}
