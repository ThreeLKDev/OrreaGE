package threelkdev.orreaGE.testing;

import org.lwjgl.glfw.GLFW;

import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.ui.animation.Animation;
import threelkdev.orreaGE.core.ui.animation.transitions.SlideTransition;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.FillConstraint;
import threelkdev.orreaGE.core.ui.constraints.PixelConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.core.ui.text.Alignment;
import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.tools.math.ValueDriver;
import threelkdev.orreaGE.tools.math.valueDrivers.ConstantDriver;
import threelkdev.orreaGE.tools.math.valueDrivers.SlideDriver;

public class LeftPanelUi extends UiComponent {
	
	private static final float EFFECT_TIME = 0.5f;
	
	private ValueDriver driver;// = new ConstantDriver( 0 );
	private ValueDriver alphaDriver = new ConstantDriver( 1 );
	private boolean displayed = true;
	
	private UiComponent bar;
	

	@Override
	protected void onInit() {
		UiBlock back = new UiBlock( UiColours.DARK_GREY );
		back.setRoundedCorners( UiTest.RADIUS );
		super.attach( back, ConstraintFactory.getFill() );
		
//		UiConstraints cons = ConstraintFactory.getDefault();
		UiConstraint x = new PixelConstraint( 15 );
		UiConstraint y = new PixelConstraint( 15 );
		UiConstraint width = new FillConstraint( 15 );
		
		Text text = Text.newText("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).")
				.align(Alignment.LEFT).setFontSize(0.6f).create();
		text.getAnimator().addTransition( new Animation( null, new SlideTransition( 0, 0.2f, 0.3f ), null, null, null ), 0, 0 );
		
		super.addText( text, x, y, width);
	}
	
	public void display( boolean on ) {
//		float anchorWidth = super.getWidthConstraint().getRelativeValue();
		if( !displayed && on ) {
			driver = new SlideDriver( getXMod(), 0, EFFECT_TIME );
			alphaDriver = new SlideDriver( 0, 1, EFFECT_TIME );
		} else if( displayed && !on ) {
			driver = new SlideDriver( getXMod(), -( super.getWidthConstraint().getRelativeValue() / 3f ), EFFECT_TIME );
			alphaDriver = new SlideDriver( 1, 0, EFFECT_TIME );
		}
		this.displayed = on;
		super.display( on );
	}
	
	@Override
	protected void updateContentsToDimensionChange( boolean scaleChange ) {
		if( !scaleChange ) {
			super.updateContentsToDimensionChange( scaleChange );
			return;
		}
		if( displayed )
			driver = new ConstantDriver( 0 );
		else {
			driver = new ConstantDriver( -( super.getWidthConstraint().getRelativeValue() / 3f ) );
		}
		super.updateContentsToDimensionChange( scaleChange );
	}
	
	@Override
	protected void updateSelf() {
		super.setAlpha( alphaDriver.update( 0.02f ) );
		if( Orrea.instance.getKeyboard().keyPressEvent( GLFW.GLFW_KEY_G ) )
			display( !displayed );
		if( Orrea.instance.getKeyboard().keyPressEvent( GLFW.GLFW_KEY_H ) ) {
			//UiConstraint posY = bar.getYConstraint();
			//posY.setPixelValue( 20 );
		}
		if( UiMaster.getMouse().isButtonDown( MouseButton.LEFT ) ) {
			int pixelX = ( int ) ( UiMaster.getMouse().getX() * UiMaster.getDisplayWidth() );
			pixelX -= ( ( UiComponent ) super.getParent() ).getPixelWidth() * super.getXConstraint().getRelativeValue();
			super.getWidthConstraint().setPixelValue( pixelX );
		}
	}

	@Override
	protected void onReInit() {
		// TODO Auto-generated method stub
		
	}

}
