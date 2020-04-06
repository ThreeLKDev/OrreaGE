package threelkdev.orreaGE.testing;

import the3lks.orreaEngine.core.ui.UiBlock;
import the3lks.orreaEngine.core.ui.UiComponent;
import the3lks.orreaEngine.core.ui.UiContainer;
import the3lks.orreaEngine.core.ui.constraints.ConstraintFactory;
import the3lks.orreaEngine.core.ui.constraints.FillConstraint;
import the3lks.orreaEngine.core.ui.constraints.PixelConstraint;
import the3lks.orreaEngine.core.ui.constraints.PixelOtherConstraint;
import the3lks.orreaEngine.core.ui.constraints.RelativeConstraint;
import the3lks.orreaEngine.core.ui.constraints.UiConstraints;
import the3lks.orreaEngine.tools.colours.Colour;

public class ScrollBarUi extends UiComponent {
	
	private OldScrollUi scrollBox;
	private SliderUi scrollBar;
	private UiContainer container;
	private boolean isHorizontal;

	public ScrollBarUi() { this( false ); }
	public ScrollBarUi( boolean horizontal ) {
		this.isHorizontal = horizontal;
	}
	
	@Override
	protected void updateSelf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onInit() {
		UiConstraints cons = ConstraintFactory.getDefault();
		scrollBox = new OldScrollUi() {
			@Override
			public void onChange() {
				scrollBar.setValue( getScroll() );
			}
		};
		cons.setWidth( new FillConstraint( 10 ) );
		cons.setHeight( new RelativeConstraint( 1 ) );
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		super.attach( scrollBox, cons );
		
		scrollBar = new SliderUi( isHorizontal ) {
			@Override
			public void onChange() {
				scrollBox.setScroll( getValue() );
			}
		};
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new PixelConstraint( 10 ) );
		cons.setHeight( new RelativeConstraint( 1 ) );
		cons.setX( new PixelOtherConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		super.attach( scrollBar, cons );
		
		Colour[] colours = new Colour[] { new Colour( 0xbbff0000 ), new Colour( 0xbb00ff00 ), new Colour( 0xbb0000ff ) };
		for( int i = 0; i < 6; i++ ) {
			UiBlock block = new UiBlock( colours[ i % colours.length ] );
			cons = ConstraintFactory.getDefault();
			cons.setWidth( new FillConstraint( 10 ) );
			cons.setHeight( new RelativeConstraint( 0.2f ) );
			cons.setX( new PixelConstraint( 10 ) );
			cons.setY( new PixelConstraint( 0 ) );
			scrollBox.attach( block, cons );
		}
		
	}
	
	
	
}
