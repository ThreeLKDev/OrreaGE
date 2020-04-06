package threelkdev.orreaGE.testing;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.inputs.Mouse.MouseClickEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseDragEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseEvent;
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
	UiStyle style;
	UiBlock range, xEnd, yEnd, bothEnd;
	//
	
	public static ScrollUi getVerticalScrollUi() { return new ScrollUi( true, false, true, VERTICAL_ALWAYS ); }
	public static ScrollUi getHorizontalScrollUi() { return new ScrollUi( true, true, false, HORIZONTAL_ALWAYS ); }
	
	public ScrollUi() { this( true, true, true, BOTH_ALWAYS ); }
	public ScrollUi( boolean showBackground, boolean scrollX, boolean scrollY, byte showScrollbars ) {
		this( showBackground, scrollX, scrollY, showScrollbars, null ); /*
			new Colour( 0xbb444444 ),
			new Colour( 0xff222222 ),
			new Colour( 0xff444444 ),
			new Colour( 0xff888888 ),
			new Colour( 0xff666666 ) ); /**/
	}
	public ScrollUi( boolean showBackground, boolean scrollX, boolean scrollY, byte showScrollbars, UiStyle uiStyle ) {
		_this = this;
		this.focusable = false;
		this.allowScrollX = scrollX;
		this.allowScrollY = scrollY;
		this.bars = showScrollbars;
		this.style = uiStyle == null ? new UiStyle() : uiStyle;
		
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
		background = new UiBlock( showBackground ? style.getBackgroundColour().duplicate() : new Colour( 0 ) ) {
			Vector2f startPos = new Vector2f();
			Vector2f startScroll = new Vector2f();
			@Override
			public void onMouseDown( MouseClickEvent e ) {
				if( e.button == MouseButton.LEFT ) {
					glideScrollX = 0;
					glideScrollY = 0;
					startPos.set( ( float ) ( e.getXAsInt() - getPixelX() ) / ( float ) getPixelWidth(), ( float ) ( e.getYAsInt() - getPixelY() ) / ( float ) getPixelHeight() );
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
		background.setInteractable( true );
		background.setClippingBounds( clipBounds );
		UiConstraints cons = ConstraintFactory.getDefault();
		cons.setWidth( new FillConstraint( 0 ) );//hasVert ? 10 : 0 ) );
		cons.setHeight( new FillConstraint( 0 ) );//hasHori ? 10 : 0 ) );
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
			public void onInitChild() {
				totalScrollX = 0f;
				totalScrollY = 0f;
				applyToUiChildren( child -> {
					if( child == range || child == xEnd || child == yEnd || child == bothEnd ) return;
					totalScrollX = Math.max( totalScrollX, child.getXConstraint().getRelativeValue() + child.getWidthConstraint().getRelativeValue() );
					totalScrollY = Math.max( totalScrollY, child.getYConstraint().getRelativeValue() + child.getHeightConstraint().getRelativeValue() );
				} );
				totalScrollX += 0.01f; //
				totalScrollY += 0.01f; //
				maxScrollX = 1f - totalScrollX; //0.985f //TODO: Move all Container code into ScrollBoxUI, wrap that with the ScrollBars
				maxScrollY = 1f - totalScrollY; //0.985f
				range.getWidthConstraint().setRelativeValue( totalScrollX );
				range.getHeightConstraint().setRelativeValue( totalScrollY );
				xEnd.getXConstraint().setRelativeValue( totalScrollX );
				yEnd.getYConstraint().setRelativeValue( totalScrollY );
				bothEnd.getXConstraint().setRelativeValue( totalScrollX );
				bothEnd.getYConstraint().setRelativeValue( totalScrollY );
				
				if( ( bars & HORIZONTAL_WHEN_NEEDED ) == HORIZONTAL_WHEN_NEEDED ) {
					if( totalScrollX > 1f ) {
						maxScrollY -= horiBar.getHeightConstraint().getRelativeValue();
						horiBar.show( true );
					} else
						horiBar.show( false );
				}
				if( ( bars & VERTICAL_WHEN_NEEDED ) == VERTICAL_WHEN_NEEDED ) {
					if( totalScrollY > 1f ) {
						maxScrollX -= vertBar.getWidthConstraint().getRelativeValue();
						vertBar.show( true );
					} else
						vertBar.show( false );
				}
			}
		};
		Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_PERIOD, () -> { 
			System.out.println( ""
					+ "X:\n"
					+ "  T:" + totalScrollX + "\n"
					+ "  M:" + maxScrollX + "\n"
					+ "  C:" + currentScrollX +"\n"
					+ "  R:" + range.getWidthConstraint().getRelativeValue() );
		});
		Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_M, () -> {
			System.out.println(
					"X:\n"
					+ "  T:" + totalScrollX + "\n"
					+ "  M:" + maxScrollX + "\n"
					+ "  C:" + currentScrollX +"\n"
					+ "Y:\n"
					+ "  T:" + totalScrollY + "\n"
					+ "  M:" + maxScrollY + "\n"
					+ "  C:" + currentScrollY +"\n"
					);
		} );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new FillConstraint( hasVert ? 10 : 0 ) );
		cons.setHeight( new FillConstraint( hasHori ? 10 : 0 ) );
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		background.attach( container, cons );
		
		if( hasVert ) {
			vertBar = new SliderUi( false, style ) {
				@Override
				public void onChange() {
					setScrollY( getValue() );
				}
			};
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new PixelConstraint( 10 ) );
			cons.setHeight( new FillConstraint( hasHori ? 10 : 0 ) );
			cons.setX( new PixelOtherConstraint( 0 ) );
			cons.setY( new PixelConstraint( 0 ) );
			if( ( bars & VERTICAL_WHEN_NEEDED ) == VERTICAL_WHEN_NEEDED ) {
				vertBar.setInitiallyHidden();
			}
			super.attach( vertBar, cons );
		}
		
		if( hasHori ) {
			horiBar = new SliderUi( true, style ) {
				@Override
				public void onChange() {
					setScrollX( getValue() );
				}
			};
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new FillConstraint( hasVert? 10 : 0 ) );
			cons.setHeight( new PixelConstraint( 10 ) );
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
			};
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new PixelConstraint( 10 ) );
			cons.setHeight( new PixelConstraint( 10 ) );
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
	protected void updateSelf() {
		if( glideScrollX != 0 || glideScrollY != 0 ) {//FIXME test if this works better now
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
		Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_X, () -> {
//			System.out.println("h: " + horiBar.getValue() + "\nv: " + vertBar.getValue() );
			scrollTo( 0, 0 );
		} );
		Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_Z, () -> {
			horiBar.setValue( 1f );
			vertBar.setValue( 1f );
		} );
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
		currentScrollX = Maths.clamp( xScroll, maxScrollX, 0f /*0.015f*/ );
		currentScrollY = Maths.clamp( yScroll, maxScrollY, 0f /*0.015f*/ );
		if( totalScrollX > 1f && allowScrollX )
			container.getXConstraint().setRelativeValue( currentScrollX );
		if( totalScrollY > 1f && allowScrollY )
			container.getYConstraint().setRelativeValue( currentScrollY );
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
	
	public void setBackgroundColour( Colour colour ) {
		this.background.setColour( colour );
	}
	
	public void setSliderBackgroundColour( Colour colour ) {
		if( horiBar != null )
			horiBar.setBackgroundColour( colour );
		if( vertBar != null )
			vertBar.setBackgroundColour( colour );
	}
	
	public void setSliderColour( Colour colour ) {
		if( horiBar != null )
			horiBar.setSliderColour( colour );
		if( vertBar != null )
			vertBar.setSliderColour( colour );
	}
	
	public void setSliderHoverColour( Colour colour ) {
		if( horiBar != null )
			horiBar.setSliderHoverColour( colour );
		if( vertBar != null )
			vertBar.setSliderHoverColour( colour );
	}
	
	public void setSliderActiveColour( Colour colour ) {
		if( horiBar != null )
			horiBar.setSliderActiveColour( colour );
		if( vertBar != null )
			vertBar.setSliderActiveColour( colour );
	}
	
}
