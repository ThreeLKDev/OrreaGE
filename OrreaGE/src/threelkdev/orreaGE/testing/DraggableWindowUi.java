package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.inputs.Mouse.MouseDragEvent;
import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;
import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.UiGroup;
import threelkdev.orreaGE.core.ui.UiMaster;
import threelkdev.orreaGE.core.ui.UiRenderBundle;
import threelkdev.orreaGE.core.ui.UiRenderData;
import threelkdev.orreaGE.core.ui.constraints.ConstraintFactory;
import threelkdev.orreaGE.core.ui.constraints.FillConstraint;
import threelkdev.orreaGE.core.ui.constraints.PixelConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.core.ui.text.Alignment;
import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.tools.colours.Colour;

public class DraggableWindowUi extends UiComponent {
	
	static final int titleBarSize = 20;
	
	UiBlock background, titleBar;
	Text titleText;
	UiGroup container;
	
	DraggableWindowUi _this;
	// dragMode true = use topBar, false = use background
	public DraggableWindowUi( String title, boolean dragMode, UiStyle style ) {
		_this = this;
		this.interactable = false;
		
		background = new UiBlock( /**/new Colour( 0xffff0000 ) ) {//*/style.getBackgroundColour() ) {
			@Override
			public void onMouseDrag( MouseDragEvent e ) {
				if( !dragMode && e.button == MouseButton.LEFT ) {
					_this.tryMove( e.diffX, e.diffY );
				}
			}
		};
		background.setInteractable( !dragMode );
		background.setBorder( style.getAccentColour(), 1f );
		UiConstraints cons = ConstraintFactory.getDefault();
		cons.setWidth( new FillConstraint( 0 ) );
		cons.setHeight( new FillConstraint( 0 ) );
		cons.setX( new PixelConstraint( 0 ) );
		cons.setY( new PixelConstraint( 0 ) );
		super.attach( background, cons );
		
		titleBar = new UiBlock( style.getForegroundColour() ) {
			@Override
			public void onMouseDrag( MouseDragEvent e ) {
				if( dragMode && e.button == MouseButton.LEFT ) {
					_this.tryMove( e.diffX, e.diffY );
				}
			}
		};
		titleBar.setInteractable( dragMode );
		cons = ConstraintFactory.getDefault();
		cons.setWidth( new FillConstraint( (int) background.getBorderWidth() ) );
		cons.setHeight( new PixelConstraint( titleBarSize ) );
		cons.setX( new PixelConstraint( (int) background.getBorderWidth() ) );
		cons.setY( new PixelConstraint( (int) background.getBorderWidth() ) );
		super.attach( titleBar, cons );
		
		titleText = Text.newText( title ).align( Alignment.LEFT ).setFontSizePixels( titleBarSize ).create();
		titleBar.addText( titleText, new PixelConstraint( 2 ), new PixelConstraint( 0 ), new FillConstraint( 25 ) );
		
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
