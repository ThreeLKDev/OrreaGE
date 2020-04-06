package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.tools.math.Maths;

public class FollowConstraint extends UiConstraint {
	
	private final UiComponent component;
	private int value;
	
	public FollowConstraint( int pixelGap, UiComponent component ) {
		this.value = pixelGap;
		this.component = component;
	}
	
	@Override
	protected void completeSetUp(UiConstraints other) { }

	@Override
	public float getRelativeValue() {
		float relValue = value / getParentPixelSize();
		float siblingRelPos = isXAxis() ? component.getXConstraint().getRelativeValue() : component.getYConstraint().getRelativeValue();
		float siblingSize = isXAxis() ? component.getWidthConstraint().getRelativeValue() : component.getHeightConstraint().getRelativeValue();
		return siblingRelPos + siblingSize + relValue;
	}

	@Override
	public void setPixelValue(int pixels) {
		if( value == pixels )
			return;
		this.value = pixels;
		notifyDimensionChange( false );
	}

	@Override
	public void setRelativeValue(float value) {
		this.value = (int) Math.floor( super.getParentPixelSize() * value );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixel( int pixels ) {
		if( pixels == 0 )
			return;
		this.value += pixels;
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelative( float value ) {
		if( value == 0 ) return;
		this.value += Math.floor( getParentPixelSize() * value );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetPixelClamp( int pixels, int min, int max ) {
		if( pixels == 0 ) return;
		this.value = Maths.clamp( this.value + pixels, min, max );
		notifyDimensionChange( false );
	}
	
	@Override
	public void offsetRelativeClamp( float value, float min, float max ) {
		if( value == 0 ) return;
		this.value = (int) Maths.clamp( this.value + Math.floor( getParentPixelSize() * value ), Math.floor( getParentPixelSize() * min ), Math.floor( getParentPixelSize() * max ) );
		notifyDimensionChange( false );
	}

}
