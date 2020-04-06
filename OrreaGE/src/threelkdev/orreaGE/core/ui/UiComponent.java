package threelkdev.orreaGE.core.ui;

import org.lwjgl.util.vector.Vector2f;

import threelkdev.orreaGE.core.engine.Node;
import threelkdev.orreaGE.core.inputs.Mouse;
import threelkdev.orreaGE.core.inputs.Mouse.MouseClickEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseDragEvent;
import threelkdev.orreaGE.core.inputs.Mouse.MouseEvent;
import threelkdev.orreaGE.core.ui.animation.Animation;
import threelkdev.orreaGE.core.ui.animation.Animator;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.TextHeightConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.tools.pools.Vec2Pool;
import threelkdev.orreaGE.tools.utils.Array;

public abstract class UiComponent extends Node{
	
	private Array< UiComponent > childrenToAdd = new Array< UiComponent >( false, 1, UiComponent.class );
	
	private UiConstraints constraints;
	
	private final Animator animator;
	
	private boolean visible = true;
	private boolean displayed = true;
	private boolean initialized = false;
	private boolean reloadOnSizeChange = false;
	private boolean hideAfterAnimation = false;

	private float xMod, yMod;
	private float x, y, width, height;
	private float selfAlpha = 1f;
	private float totalAlpha = 1f;
	private float widthMod = 1f, heightMod = 1f;
	
	private int level = 0;
	
	public UiComponent() {
		this.animator = new Animator( this );
	}
	
	public void show( boolean show ) { this.visible = show; onShow(); }
	public void onShow() {}
	
	public boolean isShown() { return visible; }
	public boolean isDisplayed() { return displayed; }
	public int getLevel() { return level; }
	public float getAbsX() { return x; }
	public float getAbsY() { return y; }
	public float getAbsWidth() { return width; }
	public float getAbsHeight() { return height; }
	public float getXMod() { return xMod; }
	public float getYMod() { return yMod; }
	public float getWidthMod() { return widthMod; }
	public float getHeightMod() { return heightMod; }
	public float getSelfAlpha() { return selfAlpha; }
	public float getTotalAlpha() { return totalAlpha; }
	public Animator getAnimator() { return animator; }
	
	public float getAbsAspectRatio() { return getPixelWidth() / getPixelHeight(); }
	public int getPixelWidth() { return Math.round( width * UiMaster.getDisplayWidth() ); }
	public int getPixelHeight() { return Math.round( height * UiMaster.getDisplayHeight() ); }
	public int getPixelX() { return Math.round( x * UiMaster.getDisplayWidth() ); }
	public int getPixelY() { return Math.round( y * UiMaster.getDisplayHeight() ); }
	public float pixelsToRelativeX( float pixels ) { return pixels / getPixelWidth(); }
	public float pixelsToRelativeY( float pixels ) { return pixels / getPixelHeight(); }
	public float getRelativeHeightCoords( float relativeWidth ) { return relativeWidth * getAbsAspectRatio(); }
	public float getRelativeWidthCoords( float relativeHeight ) { return relativeHeight / getAbsAspectRatio(); }
	
	public void onLoseFocus() {}
	public void onGainFocus() {}

	public UiConstraint getXConstraint() { return constraints.getX(); }
	public UiConstraint getYConstraint() { return constraints.getY(); }
	public UiConstraint getWidthConstraint() { return constraints.getWidth(); }
	public UiConstraint getHeightConstraint() { return constraints.getHeight(); }
	
	public void setReloadOnSizeChange() { this.reloadOnSizeChange = true; }
	
	public void onChange() {};
	
	protected boolean interactable = false;
	/** Whether this UiComponent should capture/consume mouse inputs. By default, false. Disable to allow clickthrough behaviour. */
	public boolean canInteract() { return interactable; }
	/** Set whether this UiComponent should capture/consume mouse inputs. */
	public void setInteractable( boolean interactable ) { this.interactable = interactable; }
	
	protected boolean focusable = true;
	/** Whether this UiComponent can be focused on, i.e. brought to the foreground. By default, true. */
	public boolean canFocus() { return focusable; }
	/** Set whether this UiComponent can be focused on and brought to the front. */
	public void setFocusable( boolean focusable ) { this.focusable = focusable; }
	
	public void setAlpha( float selfAlpha ) {
		this.selfAlpha = selfAlpha;
		updateTotalAlpha( ( parent instanceof UiComponent ) ? ( ( UiComponent ) parent ).totalAlpha * selfAlpha : selfAlpha );
	}
	
	public void updateTotalAlpha( float totalAlpha ) {
		this.totalAlpha = totalAlpha;
		applyToUiChildren( child -> child.updateTotalAlpha( totalAlpha * child.selfAlpha ) );
	}
	
	/** Quick and dirty convenience method to pass a custom UiComponent.Runnable<br />
	 *  and use the apply( UiComponent ) method on all child UiComponent Nodes.  */
	public void applyToUiChildren( Runnable runnable ) {
		if( children == null ) return;
		for( Node child : getArrayIterator() ) {
			if( child instanceof UiComponent ) {
				runnable.apply( ( UiComponent ) child );
			}
		}
	}
	
	public void applyToUiChildren( Runnable runnable, boolean recursive ) {
		if( recursive ) 
			applyToUiChildrenRecursive( runnable );
		else
			applyToUiChildren( runnable );
	}
	
	public void applyToUiChildrenRecursive( Runnable runnable ) {
		if( children == null ) return;
		for( Node child : getArrayIterator() ) {
			if( child instanceof UiComponent ) {
				runnable.apply( ( UiComponent ) child );
				( ( UiComponent ) child ).applyToUiChildrenRecursive( runnable );
			}
		}
	}
	
	/** Direct modification is ill advised unless you know what you're doing. */
	@Deprecated
	public void setXModDirect( float x ) { this.xMod = x;}
	public void setXMod( float x ) {
		if( x == xMod )
			return;
		this.xMod = x;
		if( initialized )
			notifyDimensionChange( false );
	}
	
	/** Direct modification is ill advised unless you know what you're doing. */
	@Deprecated
	public void setYModDirect( float y ) { this.yMod = y; }
	public void setYMod( float y ) {
		if( y == yMod )
			return;
		this.yMod = y;
		if( initialized )
			notifyDimensionChange( false );
	}
	
	/** Direct modification is ill advised unless you know what you're doing. */
	@Deprecated
	public boolean setWidthModDirect( float width ) { 
		boolean changed = this.widthMod != width;
		this.widthMod = width; 
		return changed;
	}
	public void setModifyWidth( float width ) {
		if( width == widthMod )
			return;
		this.widthMod = width;
		if ( initialized )
			notifyDimensionChange( true );
	}
	
	/** Direct modification is ill advised unless you know what you're doing. */
	@Deprecated
	public boolean setHeightModDirect( float height ) { 
		boolean changed = this.heightMod != height;
		this.heightMod = height; 
		return changed;
	}
	public void setModifyHeight( float height ) {
		if( height == heightMod )
			return;
		this.heightMod = height;
		if( initialized )
			notifyDimensionChange( true );
	}
	
	protected void moveToRoot() {
		constraints.setHeight( new RelativeConstraint( getAbsHeight() ) );
		constraints.setWidth( new RelativeConstraint( getAbsWidth() ) );
		constraints.setY( new RelativeConstraint( getAbsY() ) );
		constraints.setX( new RelativeConstraint( getAbsX() ) );
		getUiRoot().attach( this, constraints );
		//also change its attach() method to match UiMaster's add() ?
	}
	
	protected UiRoot getUiRoot() {
		if( getParent() == null || !( getParent() instanceof UiComponent ) )
			return null;
		else {
			if( getParent() instanceof UiRoot )
				return ( UiRoot ) getParent();
			else return ( ( UiComponent ) getParent() ).getUiRoot();
		}
	}
	
	protected void remove( UiComponent newParent ) {
		
	}
	
	protected void removeChild( UiComponent child, boolean delete ) {
		if( children.removeValue( child, true ) ) {
			if( delete )
				child.dispose();
			else {
				child.moveToRoot();
			}
		}
	}
	
	public void onRemove() {}
	
	protected boolean scaleChangedThisModify = false;
	public void resetModify( Animation anim ) {
		scaleChangedThisModify = false;
		if( anim.getXDriver() != null )
			this.xMod = anim.getXDriver().getHiddenValue();
		if( anim.getYDriver() != null )
			this.yMod = anim.getYDriver().getHiddenValue();
		if( anim.getWidthDriver() != null ) {
			this.widthMod = anim.getWidthDriver().getHiddenValue();
			scaleChangedThisModify = true;
		}
		if( anim.getHeightDriver() != null ) {
			this.heightMod = anim.getHeightDriver().getHiddenValue();
			scaleChangedThisModify = true;
		}
		if( anim.getAlphaDriver() != null )
			this.setAlpha( anim.getAlphaDriver().getHiddenValue() );
		notifyDimensionChange( scaleChangedThisModify );
		
	}
	
	public void setLevel( int level ) {
		this.level = level;
		applyToUiChildren( child -> {
			child.setLevel( level );
		} );
	}
	
	public float absToRelativeX( float absX ) {
		return ( absX - x ) / width;
	}
	
	public float absToRelativeY( float absY ) {
		return ( absY - y ) / height;
	}
	
	boolean isParentUi;
	protected void calculateScreenSpacePosition( boolean calcScale ) {
		isParentUi = ( parent instanceof UiComponent );
		this.x = ( isParentUi ? ( ( UiComponent ) parent ).x : 0 ) + ( ( constraints.getX().getRelativeValue() + xMod ) * ( isParentUi ? ( ( UiComponent ) parent ).width : 1f ) );
		this.y = ( isParentUi ? ( ( UiComponent ) parent ).y : 0 ) + ( ( constraints.getY().getRelativeValue() + yMod ) * ( isParentUi ? ( ( UiComponent ) parent ).height : 1f ) );
		if( calcScale ) {
			this.width = ( isParentUi ? ( ( UiComponent ) parent ).width : 1f ) * widthMod * constraints.getWidth().getRelativeValue();
			this.height = ( isParentUi ? ( ( UiComponent ) parent ).height : 1f ) * heightMod * constraints.getHeight().getRelativeValue();
		}
	}
	
	public void display() { this.display( !this.isDisplayed() ); }
	public void display( boolean display ) {
		if( display == this.displayed )
			return;
		if( display )
			show( true );
		else
			this.hideAfterAnimation = true;
		doDisplayAction( display, 0 );
		// TODO if not displayed, check when anim is over and hide;
	}
	
	public void setInitiallyHidden() {
		this.displayed = false;
		show( false );
		applyToUiChildren( child -> child.displayed = false );
	}
	
	private boolean allAnimationsFinished() {
		if( animator.isDoingAnimation() ) {
			return false;
		}
		if( children == null ) return true;
		for( Node child : children ) {
			if( child instanceof UiComponent ) {
				if( ! ( ( UiComponent ) child ).allAnimationsFinished() )
					return false;
			}
		}
		return true;
	}
	
	private void doDisplayAction( boolean display, float animationDelay ) {
		if( display == this.displayed )
			return;
		this.displayed = display;
		if( display ) {
			this.hideAfterAnimation = false;
			animator.doDisplayAnimation( animationDelay );
		} else {
			animator.doHideAnimation( animationDelay );
		}
		float delayValue = display ? animator.getDisplayDelay() : animator.getHideDelay();
		applyToUiChildren( child -> child.doDisplayAction( display, delayValue + animationDelay ) );
	}
	
	public Vector2f getMouseRelativePos() {
		Mouse mouse = UiMaster.getMouse();
		float relPosX = ( mouse.getX() - x ) / width;
		float relPosY = ( mouse.getY() - y ) / height;
		return Vec2Pool.get( relPosX, relPosY );
	}
	
	protected void updateContentsToDimensionChange( boolean scaleChange ) {
		if( scaleChange && reloadOnSizeChange ) {
			reload();
			return;
		}
		applyToUiChildren( child -> child.notifyDimensionChange( scaleChange ) );
	}
	
	public void addText( Text text, UiConstraint x, UiConstraint y, UiConstraint width ) {
		UiConstraints constraints = new UiConstraints();
		constraints.setX( x );
		constraints.setY( y );
		constraints.setWidth( width );
		constraints.setHeight( new TextHeightConstraint() );
		attach( text, constraints );
	}
	
	protected void getRenderData( UiRenderBundle renderData) {}
	
	protected final void update( UiRenderBundle renderData, float delta ) {
		if( !visible )
			return;
		
//		doMouseEvents();
		
		animator.update( delta );
		if( hideAfterAnimation && allAnimationsFinished() ) {
			show( false );
			return;
		}
		updateSelf();
		// TODO incorporate adding/removing
		getRenderData( renderData );
		applyToUiChildren( child -> child.update( renderData, delta ) );
	}
	
	/**Called when the mouse button is clicked, provided that the mouse click began on<br />
	 * this {@link #UiComponent }.
	 * @param button - The {@link #MouseButton } that was pressed, either LEFT, MIDDLE, or RIGHT.*/
	public void onMouseDown( MouseClickEvent event ) {}
	/**Called when the mouse button is released, provided that the mouse click began on<br />
	 * this {@link #UiComponent }.*/
	public void onMouseUp( MouseEvent event ) {}
	/**Called once per update while the mouse button is being held down, provided that<br />
	 * the mouse click began on this {@link #UiComponent }.*/
	public void onMouseDrag( MouseDragEvent event ) {}
	/**Called once the mouse enters this {@link #UiComponent }.*/
	public void onMouseEnter() {}
	/**Called once the mouse exits this {@link #UiComponent }.*/
	public void onMouseExit() {}
	/**Called when the mouse scrolls whilst over this {@link #UiComponent }
	 * @param scroll - -1 if the scroll wheel was scrolled 'down', 1 if up. */
	public void onMouseScroll( float scroll ) {}
	
	protected abstract void updateSelf();
	
	public void clear() {
		// TODO implement properly
		// when implementing, check how this affects text re-loading after scale change
		if( children != null ) children.clear();
	}
	
	public final void notifyDimensionChange( boolean scaleChange ) {
		calculateScreenSpacePosition( scaleChange );
		updateContentsToDimensionChange( scaleChange );
		onDimensionChange( scaleChange );
	}
	
	public void onDimensionChange( boolean scaleChange ) {}
	
	/** Simple initialization, calls onInit() then sets 'initialized' to true. */
	public void init() {
		this.onInit();
		this.initialized = true;
	}
	
	protected void forceInitialization( float absX, float absY, float absWidth, float absHeight ) {
		this.x = absX;
		this.y = absY;
		this.width = absWidth;
		this.height = absHeight;
		this.init();
	}
	
	/** Called at the end of the UiComponent's init function, just prior to setting "initialized" to true. */
	protected abstract void onInit();
	
	public void onPostInit() {}//TODO move to event section
	
	protected void reload() {
		clear();
		init();
	}
	
	public void attach( UiComponent component, UiConstraints constraints ) {
		component.constraints = constraints;
		component.constraints.notifyAdded( component, this );
		if( initialized )
			initChild( component );
		else
			childrenToAdd.add( component );
		onAttach();
	}
	
	public void onAttach() {}
	
	/** Legacy function, use only if the passed component already has UiConstraints assigned;
	 * */
//	public void attach( UiComponent component ) {
//		if( component.constraints != null ) {
//			component.constraints.notifyAdded( component, this );
//		} else {
//			throw new OrreaRuntimeException("UiComponent.attach( component ) called, but arg component lacks UiConstraints!");
//		}
//	}
	
	private void initChild( UiComponent child ) {
		this.attach( ( Node ) child );
		child.level = Math.max( level, child.level );
		child.displayed &= this.displayed;
		child.calculateScreenSpacePosition( true );
		child.init();
		child.initAllChildren();
		onInitChild();
	}
	
	public void onInitChild() { }
	
	private void initAllChildren() {
		for( UiComponent child : childrenToAdd ) {
			initChild( child );
		}
		childrenToAdd.clear();
		childrenToAdd.truncate( 0 );
		onPostInit();
	}
	
	protected void tryMove( int dX, int dY ) {
		getXConstraint().offsetPixel( dX );
		getYConstraint().offsetPixel( dY );
		notifyDimensionChange( false );
	}
	
	protected void tryMove( float dX, float dY ) {
		getXConstraint().offsetRelative( dX );
		getYConstraint().offsetRelative( dY );
		notifyDimensionChange( false );
	}
	
	public static class UiRoot extends UiComponent {
		
		public UiRoot() {
			super.forceInitialization( 0, 0, 1, 1 );
		}
		
		@Override
		protected void updateSelf() { }

		@Override
		protected void onInit() { }
		
	}
	
	public interface Runnable {
		
		public void apply( UiComponent component );
	}
	
	
}
