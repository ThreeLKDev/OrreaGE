package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.tools.colours.Colour;
import threelkdev.orreaGE.tools.math.ValueDriver;
import threelkdev.orreaGE.tools.math.valueDrivers.ConstantDriver;
import threelkdev.orreaGE.tools.math.valueDrivers.SlideDriver;

public class ButtonUi extends UiComponent {
	
	private ValueDriver offset = new ConstantDriver( 0 );
	private ValueDriver border = new ConstantDriver( 0 );
	private UiBlock square;
	
	@Override
	public void onMouseEnter() { 
		offset = new SlideDriver( getYMod(), -0.2f, 0.077f );
		border = new SlideDriver( square.getBorderWidth(), 2, 0.077f );
	}
	
	@Override
	public void onMouseExit() { 
		offset = new SlideDriver( getYMod(), 0, 0, 0.077f );
		border = new SlideDriver( square.getBorderWidth(), 0, 0.077f );
	}
	
	@Override
	protected void updateSelf() {
		super.setYMod( offset.update( 0.017f ) );
		square.setBorderWidth( border.update( 0.017f ) );
	}

	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
		square = new UiBlock( UiColours.ACCENT );
		square.setRoundedCorners( UiTest.RADIUS );
		square.setBorder( new Colour( 0f, 0f, 0f, 0.5f ), 0 );
		super.attach( square, ConstraintFactory.getFill() );
	}

	@Override
	protected void onReInit() {
		// TODO Auto-generated method stub
		
	}

}
