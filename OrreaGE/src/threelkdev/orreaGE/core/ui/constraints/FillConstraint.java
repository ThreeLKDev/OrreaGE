package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.tools.math.Maths;

/**
 * Stratches the scale of the UI to fill up the remaining space of the parent component, minus a specified pixel gap.
 *
 */
public class FillConstraint extends UiConstraint {
	//scale only
	
	private int pixelGap;
	private UiConstraint posConstraint;

	public FillConstraint( int pixelGap ) {
		this.pixelGap = pixelGap;
	}
	
	public FillConstraint() { this( 0 ); }
	
	@Override
	protected void completeSetUp(UiConstraints other) {
		this.posConstraint = isXAxis() ? other.getX() : other.getY();
		// TODO check not pos; check other constraint not aspect?
	}

	@Override
	public float getRelativeValue() {
		float relPos = posConstraint.getRelativeValue();
		float parentSizePixels = isXAxis() ? getParent().getPixelWidth() : getParent().getPixelHeight();
		float relGap = pixelGap / parentSizePixels;
		return 1f - ( relGap + relPos );
	}

	@Override
	public void setPixelValue(int pixels) {
		if( pixelGap == pixels )
			return;
		this.pixelGap = pixels;
		notifyDimensionChange( true );
	}

	@Override
	public void setRelativeValue(float value) {
		float parentSizePixels = isXAxis() ? getParent().getPixelWidth() : getParent().getPixelHeight();
		this.pixelGap = (int) Math.round( parentSizePixels * value );
		notifyDimensionChange( true );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		if( pixels == 0 ) return;
		pixelGap += pixels;
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelative( float value ) {
		if( value == 0 ) return;
		float parentSizePixels = isXAxis() ? getParent().getPixelWidth() : getParent().getPixelHeight();
		pixelGap += Math.round( parentSizePixels * value );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		if( pixels == 0 ) return;
		pixelGap = Maths.clamp( pixelGap + pixels, min, max );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		if( value == 0 ) return;
		float parentSizePixels = isXAxis() ? getParent().getPixelWidth() : getParent().getPixelHeight();
//		pixelGap = (int) Maths.clamp( pixelGap + Math.round( parentSizePixels * value ), Math.round( min * parentSizePixels ), Math.round( max * parentSizePixels ) );
		pixelGap = (int) ( Maths.clamp( pixelGap + parentSizePixels * value, min * parentSizePixels, max * parentSizePixels ) + 0.5f );
		notifyDimensionChange( false );
	}

}
