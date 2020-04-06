package threelkdev.orreaGE.testing;

import the3lks.orreaEngine.core.inputs.Mouse.MouseEvent;
import the3lks.orreaEngine.core.inputs.MouseButton;
import the3lks.orreaEngine.core.ui.UiBlock;
import the3lks.orreaEngine.core.ui.UiComponent;
import the3lks.orreaEngine.core.ui.UiMaster;
import the3lks.orreaEngine.core.ui.UiRenderData;
import the3lks.orreaEngine.core.ui.constraints.ConstraintFactory;
import the3lks.orreaEngine.core.ui.constraints.FollowConstraint;
import the3lks.orreaEngine.core.ui.constraints.PixelConstraint;
import the3lks.orreaEngine.core.ui.constraints.UiConstraints;
import the3lks.orreaEngine.tools.colours.Colour;
import the3lks.orreaEngine.tools.math.Maths;

public class OldScrollUi extends UiComponent {
	
	int[] clipBounds;
	UiBlock background;
	UiComponent first, last;
	float currentScroll = 0;
	float totalScroll = ( float ) 10 / UiMaster.getDisplayHeight();
//	float maxScroll = 0;
	OldScrollUi _this;
	
	public OldScrollUi() {
		this._this = this;
		clipBounds = new int[] { ( int ) this.getPixelX(), 
				( UiMaster.getDisplayHeight() - ( ( int ) this.getPixelY() + ( int ) this.getPixelHeight() ) ), 
				( int ) this.getPixelWidth(), ( int ) this.getPixelHeight() };
		background = new UiBlock( new Colour( 0xbb444444 ) );
		background.setClippingBounds( clipBounds );
		UiConstraints cons = ConstraintFactory.getFill();
		super.attach( background, cons );
	}
	
	@Override
	protected void updateSelf() {}

	@Override
	protected void onInit() { }
	
	@Override
	public void onPostInit() {
		clipBounds = new int[] { ( int ) background.getPixelX(), 
				( UiMaster.getDisplayHeight() - ( int ) ( background.getPixelY() + background.getPixelHeight() ) ), 
				( int ) background.getPixelWidth(), ( int ) background.getPixelHeight() };
		applyToUiChildrenRecursive( child -> {
			if( child instanceof UiRenderData ) {
				( ( UiRenderData ) child ).setClippingBounds( clipBounds );
			}
		} );
		if( first != null )
			first.getYConstraint().setRelativeValue( 0 );
		setScroll( 0 );
	}
	
	@Override
	public void onDimensionChange( boolean scaleChange ) {
		clipBounds = new int[] { ( int ) background.getPixelX(), 
				( UiMaster.getDisplayHeight() - ( int ) ( background.getPixelY() + background.getPixelHeight() ) ), 
				( int ) background.getPixelWidth(), ( int ) background.getPixelHeight() };
		applyToUiChildrenRecursive( child -> {
			if( child instanceof UiRenderData ) {
				( ( UiRenderData ) child ).setClippingBounds( clipBounds );
			}
		} );
	}
	
	@Override
	public void attach( UiComponent component, UiConstraints cons ) {
		if( last == null ) {
			cons.setY( new PixelConstraint( 5 ) );
			first = component;
		} else {
			cons.setY( new FollowConstraint( 5, last ) );
		}
		if( component instanceof UiRenderData) 
			( ( UiRenderData ) component ).setClippingBounds( clipBounds );
		super.attach( component, cons );
		last = component;
		totalScroll += cons.getHeight().getRelativeValue() + 0.01f;
	}
	
	@Override
	public void onMouseDrag( MouseEvent e ) {
		if( e.button == MouseButton.LEFT ) {
			tryScroll( ( float ) e.getDiffYi() / this.getPixelHeight() );
			onChange();
		} else {
			//tryMove( dX, dY );
		}
	}
	
	private void tryScroll( float change ) {
		if( totalScroll > 1f ) {
			currentScroll += change;
			scrollTo( currentScroll );
		}
	}
	
	@Override
	public void onMouseScroll( float scroll ) {
		tryScroll( scroll * 0.005f );
		_this.onChange();
	}
	
	public float getScroll() {
		return ( currentScroll - 0.015f ) / ( calcTotalScroll() - 0.015f );
	}
	
	private float calcTotalScroll() { 
		return 0.985f + ( first.getYConstraint().getRelativeValue() - ( last.getYConstraint().getRelativeValue() + last.getHeightConstraint().getRelativeValue() ) );
	}
	
	private void scrollTo( float value ) {
		currentScroll = Maths.clamp( value, calcTotalScroll(), 0.015f ); 
		first.getYConstraint().setRelativeValue( currentScroll );
		notifyDimensionChange( false );
	}
	
	//from 0-1
	public void setScroll( float value ) {
		scrollTo( ( value * ( calcTotalScroll() - 0.015f ) ) + 0.015f );
	}
	
}
