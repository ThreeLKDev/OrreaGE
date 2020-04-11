package threelkdev.orreaGE.core.ui;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import threelkdev.orreaGE.core.inputs.Mouse;
import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.rendering.FontRenderer;
import threelkdev.orreaGE.core.rendering.UiRenderer;
import threelkdev.orreaGE.core.ui.UiComponent.UiRoot;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.testing.UiStyle;

public class UiMaster {
	
	private static final UiRoot CONTAINER = new UiRoot();
	private static UiRenderer uiRenderer;
	private static FontRenderer fontRenderer;
	private static LinkedList< UiComponent > zOrder;
	
	private static int displayWidthPixels;
	private static int displayHeightPixels;
	private static float displayAspect;
	private static boolean displaySizeUpdated = false;
	
	private static int minWidth;
	private static int minHeight;
	
	private static float uiSizeFactor = 1f;
	private static float manualUiSize;
	
	private static Mouse currentMouse;
	
	public static UiStyle style;
	
	private static int levels = 0;
	
	/* NOTE: Changed getDisplayWidth() and getDisplayHeight() 
	 * Previously, both performed their calculations in-function
	 * Now, returns new variables displayWidth and displayHeight, respectively
	 * New vars are updated when everything else is */
	private static int displayWidth, displayHeight;
	
	public static void init( Mouse mouse, int screenWidth, int screenHeight, float manualSize ) {
		uiRenderer = new UiRenderer();
		fontRenderer = new FontRenderer();
		displayWidthPixels = screenWidth;
		displayHeightPixels = screenHeight;
		manualUiSize = manualSize;
		currentMouse = mouse;
		displayAspect = ( float ) displayWidthPixels / displayHeightPixels;
		zOrder = new LinkedList< UiComponent >();
		style = new UiStyle();
		adjustUiSize();
	}
	
	public static void setMinDisplaySize( int width, int height ) {
		minWidth = width;
		minHeight = height;
	}
	
	public static UiRoot getContainer() {
		return CONTAINER;
	}
	
	public static Mouse getMouse() {
		return currentMouse;
	}
	
	public static float getUiSize() {
		return uiSizeFactor;
	}
	
	public static int getDisplayWidth() {
		return displayWidth;
	}
	
	public static int getDisplayHeight() {
		return displayHeight;
	}
	
	public static float getDisplayAspectRatio() {
		return displayAspect;
	}
	
	public static void update( float delta ) {
		checkDisplaySizeChange();
		UiRenderBundle renderData = new UiRenderBundle();
		
//		CONTAINER.update( renderData, delta );
		for( UiComponent component : zOrder ) {
			component.update( renderData, delta);
		}
		
		updateMouse();
		
		for( RenderLevelData levelData : renderData.getRenderData() ) {
			uiRenderer.render( levelData.getUiElements(), displayWidthPixels, displayHeightPixels, uiSizeFactor, 0 );
			fontRenderer.render( levelData.getTexts(), getDisplayWidth(), getDisplayHeight() );
		}
	}
	
	
	static UiComponent focusTarget, mouseTarget, lastMouseTarget, lmbTarget, mmbTarget, rmbTarget;
	static Vector2f lmbPoint = new Vector2f(), rmbPoint = new Vector2f(), mmbPoint = new Vector2f();
	private static void updateMouse() {
		lastMouseTarget = mouseTarget;
		mouseTarget = null;
		focusTarget = null;
		for( int i = zOrder.size() - 1; i >= 0; i-- ) {
			zOrder.get( i ).applyToUiChildrenRecursive( child -> {
				if( ( !child.canFocus() && !child.canInteract() ) || !child.isDisplayed() /*|| mouseTarget != null*/ ) return;
				if( currentMouse.getX() >= child.getAbsX() && currentMouse.getX() <= child.getAbsX() + child.getAbsWidth() ) {
					if( currentMouse.getY() >= child.getAbsY() && currentMouse.getY() <= child.getAbsY() + child.getAbsHeight() ) {
						if( child.canFocus() && focusTarget == null )
							focusTarget = child;
						if( child.canInteract() ) {							
//							mouseTarget = child;
							if( mouseTarget == null )
								mouseTarget = child;
							else if( mouseTarget.getLevel() <= child.getLevel() ) {
								mouseTarget = child;
						}
						}
					}
				}
			});
//			if( mouseTarget != null ) {
//				break;
//			}
		}
		/*CONTAINER.applyToUiChildrenRecursive( child -> {
			if( !child.canFocus() || !child.isDisplayed() ) return;
			if( currentMouse.getX() >= child.getAbsX() && currentMouse.getX() <= child.getAbsX() + child.getAbsWidth() ) {
				if( currentMouse.getY() >= child.getAbsY() && currentMouse.getY() <= child.getAbsY() + child.getAbsHeight() ) {
					if( mouseTarget == null )
						mouseTarget = child;
					else if( mouseTarget.getLevel() >= child.getLevel() ) {
						mouseTarget = child;
					}
				}
			}
		});*/
		if( lastMouseTarget != mouseTarget ) {
			if( lastMouseTarget == null )
				mouseTarget.onMouseEnter();
			else if( mouseTarget == null )
				lastMouseTarget.onMouseExit();
			else {
				lastMouseTarget.onMouseExit();
				mouseTarget.onMouseEnter();
			}
		}
		
		if( focusTarget != null ) {
			if( currentMouse.isClickEvent() ) {
				focusOn( focusTarget );
			}
		}
		
		if( mouseTarget != null ) {
			if( currentMouse.isClickEvent( MouseButton.LEFT ) ) {
//				focusOn( focusTarget );
				mouseTarget.onMouseDown( currentMouse.getClickEvent( MouseButton.LEFT ) );
				lmbTarget = mouseTarget;
				lmbPoint.set( currentMouse.getX(), currentMouse.getY() );
				
			}
			if( currentMouse.isClickEvent( MouseButton.RIGHT ) ) {
//				focusOn( focusTarget );
				mouseTarget.onMouseDown( currentMouse.getClickEvent( MouseButton.RIGHT ) );
				rmbTarget = mouseTarget;
				rmbPoint.set( currentMouse.getX(), currentMouse.getY() );
				
			}
			if( currentMouse.isClickEvent( MouseButton.MIDDLE ) ) {
//				focusOn( focusTarget );
				mouseTarget.onMouseDown( currentMouse.getClickEvent( MouseButton.MIDDLE ) );
				mmbTarget = mouseTarget;
				mmbPoint.set( currentMouse.getX(), currentMouse.getY() );
				
			}
			if( currentMouse.getScroll() != 0 ) {
				mouseTarget.onMouseScroll( currentMouse.getScroll() );
			}
		}
		
		if( lmbTarget != null ) {
			if( currentMouse.isButtonDown( MouseButton.LEFT ) ) {
				lmbTarget.onMouseDrag( currentMouse.getDragEvent( MouseButton.LEFT, lmbPoint.x, lmbPoint.y ) );
			} else {
				lmbTarget.onMouseUp( currentMouse.getEvent( MouseButton.LEFT ) );
				lmbTarget = null;
			}
		}
		
		if( rmbTarget != null ) {
			if( currentMouse.isButtonDown( MouseButton.RIGHT ) ) {
				rmbTarget.onMouseDrag( currentMouse.getDragEvent( MouseButton.RIGHT, rmbPoint.x, rmbPoint.y ) );
			} else {
				rmbTarget.onMouseUp( currentMouse.getEvent( MouseButton.RIGHT ) );
				rmbTarget = null;
			}
		}
		
		if( mmbTarget != null ) {
			if( currentMouse.isButtonDown( MouseButton.MIDDLE ) ) {
				mmbTarget.onMouseDrag( currentMouse.getDragEvent( MouseButton.MIDDLE, mmbPoint.x, mmbPoint.y ) );
			} else {
				mmbTarget.onMouseUp( currentMouse.getEvent( MouseButton.MIDDLE ) );
				mmbTarget = null;
			}
		}
		
	}
	
	public static void add( UiComponent component, UiConstraints cons ) {
		component.setLevel( levels++ );
//		zOrder.push( component );
		zOrder.add( component );
		CONTAINER.attach( component, cons );
	}
	
	public static void remove( UiComponent component ) {
		//TODO
		zOrder.remove( component );
		//CONTAINER.remove( component );
	}
	
	private static UiComponent loop = null, focus = null;
	public static void focusOn( UiComponent component ) {
		if( component == null ) return;
		if( !component.canFocus() ) return;
		loop = component;
		while( loop != null && loop != CONTAINER ) {
			if( zOrder.remove( loop ) ) {
				if( focus != null ) focus.onLoseFocus();
				zOrder.add( loop );
				loop.onGainFocus();
				focus = loop;
				loop = null;
				for( int i = 0; i < zOrder.size(); i++ ) {
					zOrder.get( i ).setLevel( i );
				}
				break;
			} else { loop = loop.getParent() instanceof UiComponent ? ( UiComponent ) loop.getParent() : null; }
		}
		if( zOrder.contains( component ) ) {
			zOrder.remove( component );
			zOrder.add( component );
		}
	}
	
	public static void notifyScreenSizeChange( int width, int height ) {
		displaySizeUpdated = true;
		displayWidthPixels = width;
		displayHeightPixels = height;
		displayAspect = ( float ) displayWidthPixels / displayHeightPixels;
		adjustUiSize();
	}
	
	public static void setManualUiSize( float size ) {
		manualUiSize = size;
		displaySizeUpdated = true;
		adjustUiSize();
	}
	
	public static void cleanUp() {
		uiRenderer.cleanUp();
		fontRenderer.cleanUp();
	}
	
	private static void adjustUiSize() {
		uiSizeFactor = manualUiSize;
		if ( displayWidthPixels < minWidth * manualUiSize ) {
			uiSizeFactor = ( float ) ( displayWidthPixels) / minWidth;
		}
		if ( displayHeightPixels < minHeight * manualUiSize ) {
			uiSizeFactor = Math.min( ( float ) ( displayHeightPixels ) / minHeight, uiSizeFactor );
		}
		displayWidth = ( int ) ( displayWidthPixels / uiSizeFactor );
		displayHeight = ( int ) ( displayHeightPixels / uiSizeFactor );
	}
	
	private static void checkDisplaySizeChange() {
		if ( !displaySizeUpdated ) {
			return;
		}
		CONTAINER.applyToUiChildren( child -> child.notifyDimensionChange( true ) );
		
		displaySizeUpdated = false;
	}
	
}
