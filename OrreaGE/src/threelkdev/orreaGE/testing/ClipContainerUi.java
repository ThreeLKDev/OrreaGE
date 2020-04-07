package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.PixelConstraint;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.tools.colours.Colour;

/**
 * A UI Container with an associated clip bounds.
 * @author Luca
 *
 */
public class ClipContainerUI extends UiComponent {
	
	int[] clipBounds;
	
	float currentScrollX = 0;
	float currentScrollY = 0;
	float totalScrollX = 0;
	float totalScrollY = 0;
	float maxScrollX = 0;
	float maxScrollY = 0;
	
	//
	UiBlock range, xEnd, yEnd, bothEnd;
	//
	
	public ClipContainerUI() {
		this.focusable = false;
		clipBounds = new int[] { ( int ) this.getPixelX(),
				( UiMaster.getDisplayHeight() - ( int ) ( this.getPixelY() + this.getPixelHeight() ) ),
				( int ) this.getPixelWidth(), ( int ) this.getPixelHeight()
		};
		//
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
		cc = ConstraintFactory.getDefault()
			.setX( new PixelConstraint( 0 ) )
			.setY( new PixelConstraint( 0 ) )
			.setWidth( new PixelConstraint( 25 ) )
			.setHeight( new PixelConstraint( 25 ) );
		attach( yEnd, cc );
		bothEnd = new UiBlock( new Colour( 0x88aaaa88 ) );
		cc = ConstraintFactory.getDefault()
				.setX( new PixelConstraint( 0 ) )
				.setY( new PixelConstraint( 0 ) )
				.setWidth( new PixelConstraint( 25 ) )
				.setHeight( new PixelConstraint( 25 ) );
		attach( bothEnd, cc );
;		//
	}
	
	@Override
	public void onInitChild() {
		
	}
	
	@Override
	public void onPostInit() {
		clipBounds = new int[] { ( int ) this.getPixelX(),
			( UiMaster.getDisplayHeight() - ( int ) ( this.getPixelY() + this.getPixelHeight() ) ),
			( int ) this.getPixelWidth(), ( int ) this.getPixelHeight()
		};
	}
	
	@Override
	protected void updateSelf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
		
	}

}
