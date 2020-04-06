package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.tools.math.Maths;

public class TextHeightConstraint extends UiConstraint {
	
	private int heightPixels;
	
	@Override
	protected void completeSetUp(UiConstraints other) {}

	@Override
	public float getRelativeValue() {
		return ( float ) heightPixels / super.getParentPixelSize();
	}

	@Override
	public void setPixelValue(int pixels) {
		this.heightPixels = pixels;
	}

	@Override
	public void setRelativeValue(float value) {
		this.heightPixels = Math.round( super.getParentPixelSize() * value );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		this.heightPixels += pixels;
	}
	
	@Override
	public void offsetRelative( float value ) {
		this.heightPixels += Math.round( getParentPixelSize() * value );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		this.heightPixels = Maths.clamp( this.heightPixels + pixels, min, max );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		this.heightPixels = Maths.clamp( this.heightPixels + Math.round( getParentPixelSize() * value ) , Math.round( getParentPixelSize() * min ), Math.round( getParentPixelSize() * max ) );
	}

}
