package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.RatioConstraint;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;

public class ButtonsUi extends UiComponent {
	
	private int count;
	
	public ButtonsUi( int count ) {
		this.count = count;
		super.setReloadOnSizeChange();
	}
	
	@Override
	protected void updateSelf() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
		float width = ButtonsUi.super.getRelativeWidthCoords( 1 );
		float gap = super.pixelsToRelativeX( 5 );
		count = ( int ) ( 1 / ( width + gap ) );
		for( int i = 0; i < count; i++ ) {
			final int j = i;
			UiConstraints cons = ConstraintFactory.getDefault();
			float buttonWidth = ButtonsUi.super.getRelativeWidthCoords( 1 );
			float space = ( 1 - count * buttonWidth ) / ( count - 1 );
			cons.setX( new RelativeConstraint( ( buttonWidth + space ) * j ) );
			cons.setY( new RelativeConstraint( 0 ) );
			cons.setWidth( new RatioConstraint( 1 ) );
			cons.setHeight( new RelativeConstraint( 1 ) );
			super.attach( new ButtonUi(), cons );
		}
	}

}
