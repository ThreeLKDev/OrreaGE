package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.inputs.Mouse.MouseClickEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseDragEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseEvent;
import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.constraints.CenterConstraint;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.PixelConstraint;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.tools.colours.Colour;
import threelkdev.orreaGE.tools.math.Maths;

public class SliderUi extends UiComponent {
	
	UiBlock background, slider;
	float value, startDrag, startPos;
	int minSize = 10;
	boolean isHorizontal;
	SliderUi _this;
	
	Colour colSlider, colSliderHover, colSliderActive;
	
	
//	public SliderUi() { this( false ); }
//	public SliderUi( boolean horizontal ) {
//		this( horizontal, null );/*
//			new Colour( 0xff222222 ),
//			new Colour( 0xff444444 ),
//			new Colour( 0xff888888 ),
//			new Colour( 0xff666666 ) ); /**/
//	}
	public SliderUi( boolean horizontal, SliderUiStyle style ) {
		this.isHorizontal = horizontal;
		this._this = this;
		this.focusable = false;
		this.colSlider			= style.foregroundColour;
		this.colSliderActive	= style.activeColour;
		this.colSliderHover		= style.hoverColour;
		background = new UiBlock( /*/new Colour( 0x0 ) );//*/style.backgroundColour );
		UiConstraints cons = ConstraintFactory.getFill();
		if( isHorizontal ) {
			cons.setHeight( new PixelConstraint( style.backgroundThickness ) );
			cons.setY( new CenterConstraint() );
		} else {
			cons.setWidth( new PixelConstraint( style.backgroundThickness ) );
			cons.setX( new CenterConstraint() );
		}
		super.attach( background, cons );
		
		slider = new UiBlock( colSlider.duplicate() ) {
			boolean mouseOver = false;
			@Override
			public void onMouseEnter() {
				mouseOver = true;
				if( _this.colSliderHover != null )
					setColour( _this.colSliderHover );
			}
			@Override
			public void onMouseExit() {
				mouseOver = false;
				setColour( _this.colSlider );
			}
			float newVal, max;
			@Override
			public void onMouseDrag( MouseDragEvent e ) {
				if( e.diffX == 0 && e.diffY == 0 ) return;
				if( e.button == MouseButton.LEFT ) {
					newVal = isHorizontal ? e.relativeDragX / background.getWidth() : e.relativeDragY / background.getHeight();
					newVal = Maths.clamp( startPos + newVal, 0, 1 );
					max = isHorizontal ? background.getWidthConstraint().getRelativeValue() - getWidthConstraint().getRelativeValue() : background.getHeightConstraint().getRelativeValue() - getHeightConstraint().getRelativeValue();
					setValue( newVal / max );
					_this.onChange();
				}
			}
			@Override
			public void onMouseDown( MouseClickEvent e ) {
				if( e.button == MouseButton.LEFT ) {
					if( _this.colSliderActive != null )
						setColour( _this.colSliderActive );
					 startDrag = isHorizontal ? e.x : e.y;
					 startPos = isHorizontal ? getXConstraint().getRelativeValue() : getYConstraint().getRelativeValue();
				}
			}
			@Override
			public void onMouseUp( MouseEvent e ) {
				if( e.button == MouseButton.LEFT && _this.colSliderActive != null )
					setColour( mouseOver ? _this.colSliderHover : _this.colSlider );
			}
		};
		slider.setInteractable( true );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( isHorizontal ? new PixelConstraint( style.sliderThickness) : new RelativeConstraint( 1f ) );
		cons.setHeight( isHorizontal ? new RelativeConstraint( 1f ) : new PixelConstraint( style.sliderThickness ) );
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		super.attach( slider, cons );
	}
	
	@Override
	public String getComponentName() { return "Slider"; }
	
	@Override
	protected void updateSelf() {	}

	@Override
	protected void onInit() {
		
	}
	
	public void setMinSize( int minSize ) {
		this.minSize = minSize;
	}
	
	public float getValue() { return this.value; }
	
	public void setSliderSize( float value ) {
		if( isHorizontal ) {
			value = Maths.clamp( value, ( float ) minSize / ( float ) getPixelWidth(), 1f );
			slider.getWidthConstraint().setRelativeValue( value );
		} else {
			value = Maths.clamp( value, ( float ) minSize / ( float ) getPixelHeight(), 1f );
			slider.getHeightConstraint().setRelativeValue( value );
		}
		setValue( this.value );
	}
	
	public void setValue( float value ) {
		this.value = Maths.clamp( value, 0, 1 );
		if( isHorizontal ) {
			slider.getXConstraint().setRelativeValue( this.value * ( background.getWidthConstraint().getRelativeValue() - slider.getWidthConstraint().getRelativeValue() ) );
		} else {
			slider.getYConstraint().setRelativeValue( this.value * ( background.getHeightConstraint().getRelativeValue() - slider.getHeightConstraint().getRelativeValue() ) );
		}
	}
	
	public void setBackgroundColour( Colour colour ) {
		background.setColour( colour );
	}
	
	public void setSliderColour( Colour colour ) {
		slider.setColour( colour );
		colSlider.set( colour );
	}
	
	public void setSliderHoverColour( Colour colour ) {
		colSliderHover.set( colour );
	}
	
	public void setSliderActiveColour( Colour colour ) {
		colSliderActive.set( colour );
	}
	
	public static class SliderUiStyle {
		
		final Colour foregroundColour, activeColour, hoverColour, backgroundColour;
		final int backgroundThickness, sliderThickness;
		
		public SliderUiStyle( Colour foregroundColour, Colour activeColour, Colour hoverColour, Colour backgroundColour, int backgroundThickness, int sliderThickness ) {
			this.foregroundColour = foregroundColour;
			this.activeColour = activeColour;
			this.hoverColour = hoverColour;
			this.backgroundColour = backgroundColour;
			this.backgroundThickness = backgroundThickness;
			this.sliderThickness = sliderThickness;
		}
		
	}
	
}
