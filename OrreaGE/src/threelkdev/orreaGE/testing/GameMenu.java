package threelkdev.orreaGE.testing;

import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;
import threelkdev.orreaGE.core.ui.UiBlock;
import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.core.ui.animation.Animation;
import threelkdev.orreaGE.core.ui.animation.transitions.SlideTransition;
import threelkdev.orreaGE.core.ui.constraints.RelativeConstraint;
import threelkdev.orreaGE.core.ui.constraints.UiConstraints;
import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.tools.files.FileUtils;

public class GameMenu extends UiComponent {
	
	private static final float TIME = 0.6f;
	private static final float DELAY_SEPARATION = 0.066f;
	private static final Animation TRANSITION = new Animation()
		.heightDriver( new SlideTransition( 1, 0f, TIME ) )
		.yDriver( new SlideTransition( 0, 0.035f, TIME ) )
		.xDriver( new SlideTransition( 0, -0.8f, TIME ) )
		.alphaDriver( new SlideTransition( 1, 0, TIME ) );
	
	public GameMenu() {
		super.setLevel( 1 );
	}
	
	@Override
	protected void onInit() {
		Texture texture = Texture.newTexture( FileUtils.getResource( "gradient.png" ) ).create();
		UiBlock block = new UiBlock( texture );
		
		UiConstraints cons = new UiConstraints();
		cons.setX( new RelativeConstraint( -0.05f ) );
		cons.setY( new RelativeConstraint( -0.05f ) );
		cons.setWidth( new RelativeConstraint( 1.1f ) );
		cons.setHeight( new RelativeConstraint( 1.1f ) );
		super.attach( block, cons );
		block.getAnimator().addTransition( new Animation()
			.xDriver( new SlideTransition( 0f, 0.03f, TIME ) )
			.yDriver( new SlideTransition( 0f, 0.03f, TIME ) )
			.widthDriver( new SlideTransition( 1, 0.94f, TIME ) )
			.heightDriver( new SlideTransition( 1, 0.94f, TIME ) )
			.alphaDriver( new SlideTransition( 1, 0, TIME ) ),
		0, 0 );
		
		float delay = 0.0f;
		float yPos = 0.35f;
		for( int i = 0; i < 6; i++ ) {
			addButton( yPos, delay );
			yPos += 0.1f;
			delay += DELAY_SEPARATION;
		}
	}
	
	@Override
	protected void updateSelf() { }
	
	private void addButton( float yPos, float delay ) {
		UiBlock block = new UiBlock( UiColours.ACCENT );
		block.setRoundedCorners( 4 );
		
		UiConstraints cons = new UiConstraints();
		cons.setX( new RelativeConstraint( 0.1f ) );
		cons.setY( new RelativeConstraint( yPos ) );
		cons.setWidth( new RelativeConstraint( 0.2f ) );
		cons.setHeight( new RelativeConstraint( 0.07f ) );
		super.attach( block, cons );
		block.getAnimator().addTransition( TRANSITION, delay, delay / 3f );
		
		Text text = Text.newText( "Press Esc Key!" ).setScalable().create();
		block.addText( text, new RelativeConstraint( 0.08f ), new RelativeConstraint( 0.25f ), new RelativeConstraint( 1 ) );
		text.getAnimator().addTransition( new Animation()
				.alphaDriver( new SlideTransition( 1, 0, 0.6f ) )
				.xDriver( new SlideTransition( 0, 1.3f, 0.6f ) ), 0.1f, 0 );
		
	}

	@Override
	protected void onReInit() {
		// TODO Auto-generated method stub
		
	}
	
}
