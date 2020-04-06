package threelkdev.orreaGE.core.ui.animation;

import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.tools.math.ValueDriver;

public class Animator {
	
	private final UiComponent component;
	
	private Animation displayAnimation;
	private float displayDelay = 0;
	private Animation hideAnimation;
	private float hideDelay = 0;
	
	private ValueDriver xDriver, yDriver, 
	widthDriver, heightDriver, alphaDriver;
	
	private boolean active = false; //TODO: make inactive again
	
	public Animator( UiComponent component ) {
		this.component = component;
	}
	
	@SuppressWarnings("deprecation")
	public void resetModifiersToHidden() {
		boolean scaleChange = false;
		if( displayAnimation == null )
			return;
		if( displayAnimation.getXDriver() != null )
			component.setXModDirect( displayAnimation.getXDriver().getHiddenValue() );
		if( displayAnimation.getYDriver() != null )
			component.setYModDirect( displayAnimation.getYDriver().getHiddenValue() );
		if( displayAnimation.getWidthDriver() != null ) 
			scaleChange |= component.setWidthModDirect( displayAnimation.getWidthDriver().getHiddenValue() );
		if( displayAnimation.getHeightDriver() != null )
			scaleChange |= component.setHeightModDirect( displayAnimation.getHeightDriver().getHiddenValue() );
		if( displayAnimation.getAlphaDriver() != null )
			component.setAlpha( displayAnimation.getAlphaDriver().getHiddenValue() );
		component.notifyDimensionChange( scaleChange );
	}
	
	public void addDisplayTransition( Animation animation, float delay ) {
		this.displayAnimation = animation;
		this.displayDelay = delay;
	}
	
	public void addHideTransition( Animation animation, float delay ) {
		this.hideAnimation = animation;
		this.hideDelay = delay;
	}
	
	public void addTransition( Animation animation, float displayDelay, float hideDelay ) {
		this.displayAnimation = animation;
		this.hideAnimation = animation;
		this.displayDelay = displayDelay;
		this.hideDelay = hideDelay;
	}
	
	public boolean isDoingAnimation() { return active; }
	public float getDisplayDelay() { return displayDelay; }
	public float getHideDelay() { return hideDelay; }
	
	public void doDisplayAnimation( float parentDelay ) {
		if( displayAnimation == null ) {
			this.active = false;
			return;
		}
		if( !active )
			resetModifiersToHidden();
		applyAnimation( displayAnimation, parentDelay + displayDelay, true );
	}
	
	public void doHideAnimation( float parentDelay ) {
		if( hideAnimation == null ) {
			this.active = false;
			return;
		}
		applyAnimation( hideAnimation, parentDelay + hideDelay, false );
	}
	
	@SuppressWarnings("deprecation")
	public void update( float delta ) {
		if( !active ) return;
		boolean finished = true;
		boolean scaleChange = false;
		if( xDriver != null ) {
			component.setXModDirect( xDriver.update( delta ) );
			finished &= xDriver.hasCompletedOnePeriod();
		}
		if( yDriver != null ) {
			component.setYModDirect( yDriver.update( delta ) );
			finished &= yDriver.hasCompletedOnePeriod();
		}
		if( widthDriver != null ) {
			scaleChange |= component.setWidthModDirect( widthDriver.update( delta ) );
			finished &= widthDriver.hasCompletedOnePeriod();
		}
		if( heightDriver != null ) {
			scaleChange |= component.setHeightModDirect( heightDriver.update( delta ) );
			finished &= heightDriver.hasCompletedOnePeriod();
		}
		if( alphaDriver != null ) {
			component.setAlpha( alphaDriver.update( delta ) );
			finished &= alphaDriver.hasCompletedOnePeriod();
		}
		component.notifyDimensionChange( scaleChange );
		this.active = !finished;
	}
	
	private void applyAnimation( Animation animation, float delay, boolean display ) {
		this.active = true;
		this.xDriver = animation.initXDriver( display, component.getXMod(), delay );
		this.yDriver = animation.initYDriver( display, component.getYMod(), delay );
		this.widthDriver = animation.initWidthDriver( display, component.getWidthMod(), delay );
		this.heightDriver = animation.initHeightDriver( display, component.getHeightMod(), delay );
		this.alphaDriver = animation.initAlphaDriver( display, component.getSelfAlpha(), delay );
	}
	
}
