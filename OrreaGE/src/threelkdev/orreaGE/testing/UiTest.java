package threelkdev.orreaGE.testing;

import org.lwjgl.glfw.GLFW;

import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.ui.constraints.CenterConstraint;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.PixelConstraint;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.core.ui.text.Alignment;
import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.tools.colours.Colour;

public class UiTest {
	
	public static float RADIUS = 5;
	
	static Text fpsCounter;
	
	//
	static int x = 0, y = 0;
	//
	
	public static void createUI() {
		
		UiConstraints cons = ConstraintFactory.getDefault();
		/*
		UiComponent redBlock = new LeftPanelUi();
		PixelConstraint x = new PixelConstraint( 20 );
		cons.setX( x );
		cons.setY( new RatioConstraint( 1f ) );
		cons.setWidth( new PixelConstraint( 300 ) );
		cons.setHeight( new FillConstraint( 80 ) );
		
		UiMaster.getContainer().attach( redBlock, cons );
		
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelOtherConstraint( 25 ) );
		cons.setY( new PixelConstraint( 25 ) );
		cons.setWidth( new PixelConstraint( 160 ) );
		cons.setHeight( new RatioConstraint( 1f ) );
		UiBlock square = new UiBlock( UiColours.DARK_GREY );
		square.setBorder( new Colour( 0, 1, 1, 1 ), 2 );
		square.setRoundedCorners( UiTest.RADIUS );
		square.setInitiallyHidden();
		UiMaster.getContainer().attach( square, cons );
		square.getAnimator().addTransition( new Animation( 
				new SlideTransition( 0, 0.05f, 0.26f ), null, null, null,
				new SlideTransition( 1, 0, 0.26f ) ), 0, 0 );
		
		Animation trans = new Animation().widthDriver( new SlideTransition( 1, 0, 0.26f ) );
		float time = 0;
		float extra = 0.05f;
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelConstraint( 10 ) );
		cons.setY( new PixelConstraint( 10 ) );
		cons.setWidth( new FillConstraint( 10 ) );
		cons.setHeight( new PixelConstraint( 25 ) );
		UiBlock block1 = new UiBlock( UiColours.ACCENT );
		block1.getAnimator().addDisplayTransition( trans, time );
		square.attach( block1, cons );
		
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelConstraint( 10 ) );
		cons.setY( new FollowConstraint( 10, block1 ) );
		cons.setWidth( new FillConstraint( 10 ) );
		cons.setHeight( new PixelConstraint( 25 ) );
		UiBlock block2 = new UiBlock( UiColours.ACCENT );
		block2.getAnimator().addDisplayTransition( trans, time += extra );
		square.attach( block2, cons );
		
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelConstraint( 10 ) );
		cons.setY( new FollowConstraint( 10, block2 ) );
		cons.setWidth( new FillConstraint( 10 ) );
		cons.setHeight( new PixelConstraint( 25 ) );
		UiBlock block3 = new UiBlock( UiColours.ACCENT );
		block3.getAnimator().addDisplayTransition( trans, time += extra );
		square.attach( block3, cons );
		
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelConstraint( 10 ) );
		cons.setY( new FollowConstraint( 10, block3 ) );
		cons.setWidth( new FillConstraint( 10 ) );
		cons.setHeight( new PixelConstraint( 25) );
		UiBlock block4 = new UiBlock( UiColours.ACCENT );
		block4.getAnimator().addDisplayTransition( trans, time += extra );
		square.attach( block4, cons );
		
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelOtherConstraint( 25 ) );
		cons.setY( new PixelConstraint( 200 ) );
		cons.setWidth( new PixelConstraint( 160 ) );
		cons.setHeight( new PixelConstraint( 50 ) );
		UiBlock green = new UiBlock( UiColours.ACCENT ) {
			@Override
			protected void updateSelf() {
				if( UiMaster.getMouse().isClickEvent( MouseButton.MIDDLE ) ) {
					square.display( !square.isDisplayed() );
				}
			}
		}; 
		
		green.setRoundedCorners( UiTest.RADIUS );
		UiMaster.getContainer().attach( green, cons );
		
		cons = ConstraintFactory.getDefault();
		cons.setX( new CenterConstraint() );
		cons.setY( new PixelOtherConstraint( 20 ) );
		cons.setWidth( new PixelConstraint( 360 ) );
		cons.setHeight( new RatioConstraint( 0.1f ) );
		UiMaster.getContainer().attach( new ButtonsUi( 5 ), cons );
		 */
		
		ScrollUi scroll = new ScrollUi();
		cons.setHeight( new PixelConstraint( 600 ) );
		cons.setWidth( new PixelConstraint( 600 ) );
		cons.setX( new CenterConstraint() );
		cons.setY( new CenterConstraint() );
		UiMaster.add( scroll, cons );
		
		Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_H, () -> {
			if( x == 0 ) y++;
			UiBlock block = new UiBlock( new Colour( x % 2 == 0 ? 0xff888888 : 0xffaaaaaa ) );
			UiConstraints con = ConstraintFactory.getDefault();
			con.setWidth( new PixelConstraint( 100 ) );
			con.setHeight( new PixelConstraint( 100 ) );
			con.setX( new PixelConstraint( 5 + ( x++ * 105 ) ) );
			con.setY( new PixelConstraint( 5 ) );
			scroll.attach( block, con );
		} );
		Orrea.instance.addKeyPressListener( GLFW.GLFW_KEY_J, () -> {
			if( y == 0 ) x++;
			UiBlock block = new UiBlock( new Colour( y % 2 == 0 ? 0xff888888 : 0xffaaaaaa ) );
			UiConstraints con = ConstraintFactory.getDefault();
			con.setWidth( new PixelConstraint( 100 ) );
			con.setHeight( new PixelConstraint( 100 ) );
			con.setX( new PixelConstraint( 5 ) );
			con.setY( new PixelConstraint( 5 + ( y++ * 105 ) ) );
			scroll.attach( block, con );
		} );
		/*
		UiBlock red = new UiBlock( new Colour( 0xffff0000 ) );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new PixelConstraint( 100 ) );
		cons.setHeight( new PixelConstraint( 100 ) );
		cons.setY( new PixelConstraint( 5 ) );
		cons.setX( new RelativeConstraint( 0.5f ) );
		scroll.attach( red, cons );
		
		UiBlock blue = new UiBlock( new Colour( 0xff0000ff ) );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new PixelConstraint( 75 ) );
		cons.setHeight( new PixelConstraint( 100 ) );
		cons.setY( new PixelConstraint( 200 ) );
		cons.setX( new RelativeConstraint( 2f ) );
		scroll.attach( blue, cons );
		
		UiBlock green = new UiBlock( new Colour( 0xff00ff00 ) );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new PixelConstraint( 120 ) );
		cons.setHeight( new PixelConstraint( 200 ) );
		cons.setX( new PixelConstraint( 100 ) );
		cons.setY( new RelativeConstraint( 3f ) );
		scroll.attach( green, cons );
		*/
		
		UiBlock bg = new UiBlock( new Colour( 0x44000000 ) );
		cons = ConstraintFactory.getDefault();
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		cons.setWidth( new PixelConstraint( 200 ) );
		cons.setHeight( new PixelConstraint( 50 ) );
		UiMaster.add( bg, cons);
		fpsCounter = Text.newText( "FPS: " ).align( Alignment.LEFT ).create();
		bg.addText( fpsCounter, new PixelConstraint( 5 ), new PixelConstraint( 0 ), new RelativeConstraint( 1 ) );
		
		DraggableWindowUi window = new DraggableWindowUi( "Test Title", true, new UiStyle() );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new PixelConstraint( 200 ) );
		cons.setHeight( new PixelConstraint( 200 ) );
		cons.setX( new PixelConstraint( 20 ) );
		cons.setY( new PixelConstraint( 50 ) );
		UiMaster.add( window, cons);
		
		
	}
	
	private static float d = 0f;
	public static void displayFPS( int fps, float delta ) {
		d += delta;
		if( d >= 1f ) {
			d %= 1;
			fpsCounter.set( "FPS: " + fps );
		}
	}
	
}
