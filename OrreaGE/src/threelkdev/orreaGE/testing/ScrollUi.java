package threelkdev.orreaGE.testing;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import threelkdev.orreaGE.core.inputs.Mouse.MouseClickEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseDragEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseEvent;
import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.UiGroup;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.ui.UiRenderData;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.FillConstraint;
import threelkdev.orreaGE.core.ui.constraints.PixelConstraint;
import threelkdev.orreaGE.core.ui.constraints.PixelOtherConstraint;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.testing.SliderUi.SliderUiStyle;
import threelkdev.orreaGE.tools.colours.Colour;
import threelkdev.orreaGE.tools.math.Maths;
import threelkdev.orreaGE.tools.math.RollingAverage;

public class ScrollUi extends UiComponent {
	
	public static final byte HIDE_BOTH		 		= 0b0000;
	public static final byte VERTICAL_WHEN_NEEDED	= 0b0001;
	public static final byte HORIZONTAL_WHEN_NEEDED	= 0b0010;
	public static final byte VERTICAL_ALWAYS		= 0b0100;
	public static final byte HORIZONTAL_ALWAYS		= 0b1000;
	public static final byte BOTH_WHEN_NEEDED		= 0b0011;
	public static final byte BOTH_ALWAYS			= 0b1100;
	
	public static final float NO_GLIDE	= 0;
	
	int[] clipBounds;
	UiComponent container;
	UiBlock background, separator;
	float currentScrollX = 0, currentScrollY = 0;
	float totalScrollX = 0, totalScrollY = 0;
	float maxScrollX = 0, maxScrollY = 0;
	float glideScrollX = 0, glideScrollY = 0, glideDampening = 0.98f, glideMin = 0.001f;
	float glideMinSquared = glideMin * glideMin;
	RollingAverage avgGlideX, avgGlideY;
	boolean allowScrollX, allowScrollY;
	byte bars;
	SliderUi vertBar, horiBar;
	ScrollUi _this;
	
	//
	ScrollUiStyle style;
	UiBlock range, xEnd, yEnd, bothEnd;
	//
	
	public static ScrollUi getVerticalScrollUi() { return new ScrollUi( true, false, true, VERTICAL_ALWAYS ); }
	public static ScrollUi getHorizontalScrollUi() { return new ScrollUi( true, true, false, HORIZONTAL_ALWAYS ); }
	
	public ScrollUi() { this( true, true, true, BOTH_WHEN_NEEDED ); }
	public ScrollUi( boolean showBackground, boolean scrollX, boolean scrollY, byte showScrollbars ) {
		this( showBackground, scrollX, scrollY, showScrollbars, new ScrollUiStyle( 
				new SliderUiStyle(
						new Colour( 0xff444444 ),
						new Colour( 0xff666666 ),
						new Colour( 0xff555555 ),
						new Colour( 0xff333333 ),
						3, 11
						), new Colour( 0xaa444444 ) ) );
	}
	public ScrollUi( boolean showBackground, boolean scrollX, boolean scrollY, byte showScrollbars, ScrollUiStyle style ) {
		_this = this;
		this.focusable = false;
		this.allowScrollX = scrollX;
		this.allowScrollY = scrollY;
		this.bars = showScrollbars;
		this.style = style;
		
		if( glideDampening > 0 ) {
			avgGlideX = new RollingAverage( 8 );
			avgGlideY = new RollingAverage( 8 );
		}
		
		boolean hasVert = ( bars & VERTICAL_WHEN_NEEDED ) == VERTICAL_WHEN_NEEDED || ( bars & VERTICAL_ALWAYS ) == VERTICAL_ALWAYS;
		boolean hasHori = ( bars & HORIZONTAL_WHEN_NEEDED ) == HORIZONTAL_WHEN_NEEDED || ( bars & HORIZONTAL_ALWAYS ) == HORIZONTAL_ALWAYS;
		
		clipBounds = new int[] { ( int ) this.getPixelX(),
				( UiMaster.getDisplayHeight() - ( int ) ( this.getPixelY() + this.getPixelHeight() ) ),
				( int ) this.getPixelWidth(), ( int ) this.getPixelHeight()
		};
		background = new UiBlock( showBackground ? style.backgroundColour.duplicate() : new Colour( 0 ) ) {
//			Vector2f startPos = new Vector2f();
			Vector2f startScroll = new Vector2f();
			@Override
			public void onMouseDown( MouseClickEvent e ) {
				if( e.button == MouseButton.LEFT ) {
					glideScrollX = 0;
					glideScrollY = 0;
//					startPos.set( ( float ) ( e.getXAsInt() - getPixelX() ) / ( float ) getPixelWidth(), ( float ) ( e.getYAsInt() - getPixelY() ) / ( float ) getPixelHeight() );
					startScroll.set( currentScrollX, currentScrollY );//getScrollX(), getScrollY() );
				}
			}
			float diffX, diffY;
			@Override
			public void onMouseDrag( MouseDragEvent e ) {
				if( e.button == MouseButton.LEFT ) {
					diffX = e.relativeDragX / getWidth();
					diffY = e.relativeDragY / getHeight();
					scrollTo( Maths.clamp( startScroll.x + diffX, maxScrollX, 0f /*0.015f*/ ), Maths.clamp( startScroll.y + diffY, maxScrollY, 0f /*0.015f*/ ) );
					if( horiBar != null )
						horiBar.setValue( getScrollX() );
					if( vertBar != null )
						vertBar.setValue( getScrollY() );
					if( glideDampening > 0 ) {
						if( glideDampening > 0 ) {
							avgGlideX.addValue( e.diffX );
							avgGlideY.addValue( e.diffY );
						}
					}
				} else if( e.button == MouseButton.MIDDLE ) {
					_this.tryMove( e.diffX, e.diffY );
				}
			}
			@Override
			public void onMouseUp( MouseEvent e ) {
				if( e.button == MouseButton.LEFT ) {
					if( glideDampening > 0 ) {
						glideScrollX = avgGlideX.calculate();
						glideScrollY = avgGlideY.calculate();
					}
				}
			}
		};
		background.setName( "Background" );
		background.setInteractable( true );
		background.setClippingBounds( clipBounds );
		UiConstraints cons = ConstraintFactory.getDefault();
		cons.setWidth( new FillConstraint( hasVert ? style.vertBarStyle.getThickness() : 0 ) );
		cons.setHeight( new FillConstraint( hasHori ? style.horiBarStyle.getThickness() : 0 ) );
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		super.attach( background, cons );
		
		container = new UiGroup() {
			@Override
			public void onInit() {
				range = new UiBlock( new Colour( 0x88888888 ) );
				UiConstraints cc = ConstraintFactory.getDefault();
				cc.setX( new PixelConstraint( 0 ) );
				cc.setY( new PixelConstraint( 0 ) );
				cc.setWidth( new RelativeConstraint( 1f ) );
				cc.setHeight( new RelativeConstraint( 1f ) );
				attach( range, cc );
				xEnd = new UiBlock( new Colour( 0x88aa8888 ) );
				cc = ConstraintFactory.getDefault();
				cc.setX( new PixelConstraint( 0 ) );
				cc.setY( new PixelConstraint( 0 ) );
				cc.setWidth( new PixelConstraint( 25 ) );
				cc.setHeight( new PixelConstraint( 25 ) );
				attach( xEnd, cc );
				yEnd = new UiBlock( new Colour( 0x8888aa88 ) );
				cc = ConstraintFactory.getDefault();
				cc.setX( new PixelConstraint( 0 ) );
				cc.setY( new PixelConstraint( 0 ) );
				cc.setWidth( new PixelConstraint( 25 ) );
				cc.setHeight( new PixelConstraint( 25 ) );
				attach( yEnd, cc );
				bothEnd = new UiBlock( new Colour( 0x88aaaa88 ) );
				cc = ConstraintFactory.getDefault();
				cc.setX( new PixelConstraint( 0 ) );
				cc.setY( new PixelConstraint( 0 ) );
				cc.setWidth( new PixelConstraint( 25 ) );
				cc.setHeight( new PixelConstraint( 25 ) );
				attach( bothEnd, cc );
			}
			@Override
			public void onInitChild( UiComponent childComponent ) {
				resizeContents();
			}
			@Override
			public void onChildDetached() {
				resizeContents();
			}
			@Override
			public void customNotify( String note ) {
				if( note == "resizeContents" )
					resizeContents();
			}
			private void resizeContents() {
				totalScrollX = 0f;
				totalScrollY = 0f;
				applyToUiChildren( child -> {
					if( child == range || child == xEnd || child == yEnd || child == bothEnd ) return;
					totalScrollX = Math.max( totalScrollX, child.getXConstraint().getRelativeValue() + child.getWidthConstraint().getRelativeValue() );
					totalScrollY = Math.max( totalScrollY, child.getYConstraint().getRelativeValue() + child.getHeightConstraint().getRelativeValue() );
				} );
				totalScrollX += 0.01f; //
				totalScrollY += 0.01f; //
				maxScrollX = 1f - totalScrollX; //0.985f 
				maxScrollY = 1f - totalScrollY; //0.985f
				range.getWidthConstraint().setRelativeValue( totalScrollX );
				range.getHeightConstraint().setRelativeValue( totalScrollY );
				xEnd.getXConstraint().setRelativeValue( totalScrollX );
				yEnd.getYConstraint().setRelativeValue( totalScrollY );
				bothEnd.getXConstraint().setRelativeValue( totalScrollX );
				bothEnd.getYConstraint().setRelativeValue( totalScrollY );
				
				if( ( bars & HORIZONTAL_WHEN_NEEDED ) == HORIZONTAL_WHEN_NEEDED ) {
					if( horiBar.isShown() ) {
						if( totalScrollX <= 1f ) {
							horiBar.show( false );
//							background.getHeightConstraint().offsetPixel( -style.horiBarStyle.getThickness() );
						} 
					} else {
						if( totalScrollX > 1f ) {
							horiBar.show( true );
//							background.getHeightConstraint().offsetPixel( style.horiBarStyle.getThickness() );
						} 
					}
				}
				if( ( bars & VERTICAL_WHEN_NEEDED ) == VERTICAL_WHEN_NEEDED ) {
					if( vertBar.isShown() ) {
						if( totalScrollY <= 1f )
							vertBar.show( false );
						else
							vertBar.show( true );
					} else {
						if( totalScrollY > 1f )
							vertBar.show( true );
						else
							vertBar.show( false );
					}
				}
				if( vertBar.isShown() && horiBar.isShown() ) {
						separator.show( true);
				} else separator.show( false );
				scrollTo( currentScrollX, currentScrollY );
			}
		};
		container.setName( "Container" );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new FillConstraint( 0 ) );
		cons.setHeight( new FillConstraint( 0 ) );
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		background.attach( container, cons );
		
		if( hasVert ) {
			vertBar = new SliderUi( false, style.vertBarStyle ) {
				@Override
				public void onChange() {
					setScrollY( getValue() );
				}
			};
			vertBar.setName( "Vertical Scrollbar" );
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new PixelConstraint( style.vertBarStyle.getThickness() ) );
			cons.setHeight( new FillConstraint( hasHori ? style.horiBarStyle.getThickness() : 0 ) );
//			cons.setHeight( new RelativeConstraint( 1f ) );
			cons.setX( new PixelOtherConstraint( 0 ) );
			cons.setY( new PixelConstraint( 0 ) );
			if( ( bars & VERTICAL_WHEN_NEEDED ) == VERTICAL_WHEN_NEEDED ) {
				vertBar.setInitiallyHidden();
			}
			super.attach( vertBar, cons );
		}
		
		if( hasHori ) {
			horiBar = new SliderUi( true, style.horiBarStyle ) {
				@Override
				public void onChange() {
					setScrollX( getValue() );
				}
			};
			horiBar.setName( "Horizontal Scrollbar" );
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new FillConstraint( hasVert? style.vertBarStyle.getThickness() : 0 ) );
			cons.setHeight( new PixelConstraint( style.horiBarStyle.getThickness() ) );
			cons.setX( new PixelConstraint( 0 ) );
			cons.setY( new PixelOtherConstraint( 0 ) );
			if( ( bars & HORIZONTAL_WHEN_NEEDED ) == HORIZONTAL_WHEN_NEEDED ) {
				horiBar.setInitiallyHidden();
			}
			super.attach( horiBar, cons );
		}
		
		if( hasVert && hasHori ) {
			separator = new UiBlock( new Colour( 0xff333333 ) ) {
				@Override
				public int[] getClippingBounds() {
					return null;
				}
				@Override
				public void onMouseDown( MouseClickEvent e ) {
					
				}
				@Override
				public void onMouseDrag( MouseDragEvent e ) {
					if( e.button == MouseButton.LEFT ) {
						_this.getWidthConstraint().offsetRelative( e.diffX );
						_this.getHeightConstraint().offsetRelative( e.diffY );
						notifyDimensionChange( false );
						applyClipBounds();
					}
				}
				@Override
				public void onMouseUp( MouseEvent e ) {
					if( e.button == MouseButton.LEFT )
						container.customNotify( "resizeContents" );
				}
			};
			separator.setName( "Scrollbar Separator" );
			separator.setInteractable( true );
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new PixelConstraint( style.vertBarStyle.getThickness() ) );
			cons.setHeight( new PixelConstraint( style.horiBarStyle.getThickness() ) );
			cons.setX( new PixelOtherConstraint( 0 ) );
			cons.setY( new PixelOtherConstraint( 0 ) );
			if( ( bars & HORIZONTAL_WHEN_NEEDED ) == HORIZONTAL_WHEN_NEEDED ||
				( bars & VERTICAL_WHEN_NEEDED ) == VERTICAL_WHEN_NEEDED ) {
				separator.setInitiallyHidden();
			}
			super.attach( separator, cons );
		}
	}
	
	@Override
	public String getComponentName() { return "Scrollbox"; }
	
	@Override
	protected void updateSelf() {
		if( glideScrollX != 0 || glideScrollY != 0 ) {
			tryScroll( glideScrollX, glideScrollY );
			glideScrollX = glideScrollX * glideScrollX > glideMinSquared ? glideScrollX * glideDampening : 0;
			glideScrollY = glideScrollY * glideScrollY > glideMinSquared ? glideScrollY * glideDampening : 0;
			if( horiBar != null )
				horiBar.setValue( getScrollX() );
			if( vertBar != null )
				vertBar.setValue( getScrollY() );
		}
	}

	@Override
	protected void onInit() {	}
	
	@Override
	public void onPostInit() {
		clipBounds = new int[] { ( int ) background.getPixelX(),
				( UiMaster.getDisplayHeight() - ( int ) ( background.getPixelY() + background.getPixelHeight() ) ),
				( int ) background.getPixelWidth(), ( int ) background.getPixelHeight()
		};
		applyClipBounds();
		setScroll( 0, 0 );
	}
	
	private void applyClipBounds() {
		applyToUiChildren( child -> {
			if( !( child instanceof SliderUi ) ) {
				if( child instanceof UiRenderData )
					( ( UiRenderData ) child ).setClippingBounds( clipBounds );
				child.applyToUiChildrenRecursive( desc -> {
					if( desc instanceof UiRenderData )
						( ( UiRenderData ) desc ).setClippingBounds( clipBounds );
				} );
			}
		} );
	}
	
	@Override
	public void onDimensionChange( boolean scaleChange ) {
		clipBounds[ 0 ] = ( int ) background.getPixelX();
		clipBounds[ 1 ] = ( UiMaster.getDisplayHeight() - ( int ) ( background.getPixelY() + background.getPixelHeight() ) );
		clipBounds[ 2 ] = ( int ) background.getPixelWidth();
		clipBounds[ 3 ] = ( int ) background.getPixelHeight();
		applyClipBounds();
		if( horiBar != null ) {
			horiBar.setSliderSize( 1f / totalScrollX );
		}
		if( vertBar != null ) {
			vertBar.setSliderSize( 1f / totalScrollY );
		}
	}
	
	@Override
	public void attach( UiComponent component, UiConstraints cons ) {
		container.attach( component, cons );
		if( horiBar != null ) {
			horiBar.setSliderSize( 1f / totalScrollX );
		}
		if( vertBar != null ) {
			vertBar.setSliderSize( 1f / totalScrollY );
		}
		if( component instanceof UiRenderData )
			( ( UiRenderData ) component ).setClippingBounds( clipBounds );
		component.applyToUiChildrenRecursive( child -> {
			if( child instanceof UiRenderData )
				( ( UiRenderData ) child ).setClippingBounds( clipBounds );
		} );
	}
	
	boolean changed;
	private void tryScroll( float xChange, float yChange ) {
		changed = false;
		if( totalScrollX > 1f && xChange != 0 ) {
			currentScrollX += xChange;
			changed = true;
		}
		if( totalScrollY > 1f && yChange != 0 ) {
			currentScrollY += yChange;
			changed = true;
		}
		if( changed ) scrollTo( currentScrollX, currentScrollY );
	}
	
	public float getScrollX() {
		return ( currentScrollX - 0f /*0.015f*/ ) / maxScrollX;
	}
	
	public float getScrollY() {
		return ( currentScrollY - 0f /*0.015f*/ ) / maxScrollY;
	}
	
	private void scrollTo( float xScroll, float yScroll ) {
		currentScrollX = Maths.clamp( xScroll, maxScrollX <= 0f ? maxScrollX : 0f, 0f /*0.015f*/ );
		currentScrollY = Maths.clamp( yScroll, maxScrollY <= 0f ? maxScrollY : 0f, 0f /*0.015f*/ );
		if( allowScrollX )
			container.getXConstraint().setRelativeValue( ( float ) ( currentScrollX * container.getPixelWidth() ) / ( float ) background.getPixelWidth() );
		if( allowScrollY )
			container.getYConstraint().setRelativeValue( ( float ) ( currentScrollY * container.getPixelHeight() ) / ( float ) background.getPixelHeight() );
		notifyDimensionChange( false );
	}
	
//	private void directScroll( float xScroll, float yScroll ) {
//		
//	}
	
	public void setScroll( float x, float y ) {
		scrollTo( ( x * ( maxScrollX - 0f /*0.015f*/ ) ) + 0f /*0.015f*/ ,
				  ( y * ( maxScrollY - 0f /*0.015f*/ ) ) + 0f /*0.015f*/ );
	}
	public void setScrollX( float x ) { scrollTo( ( x * ( maxScrollX - 0f /*0.015f*/ ) ) + 0f /*0.015f*/, currentScrollY ); }
	public void setScrollY( float y ) { scrollTo( currentScrollX, ( y * ( maxScrollY - 0f /*0.015f*/ ) ) + 0f /*0.015f*/ ); }
	
	
	public static class ScrollUiStyle {
		
		final SliderUiStyle vertBarStyle, horiBarStyle;
		
		final Colour backgroundColour;
		
		public ScrollUiStyle( SliderUiStyle sliderStyle, Colour backgroundColour ) {
			this( sliderStyle, sliderStyle, backgroundColour );
		}
		public ScrollUiStyle( SliderUiStyle vertBarStyle, SliderUiStyle horiBarStyle, Colour backgroundColour ) {
			this.vertBarStyle = vertBarStyle;
			this.horiBarStyle = horiBarStyle;
			this.backgroundColour = backgroundColour;
		}
		
	}


	@Override
	protected void onReInit() {
		// TODO Auto-generated method stub
		
	}
	
}
