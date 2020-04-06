package threelkdev.orreaGE.core.ui.constraints;

public class ConstraintFactory {
	
	public static UiConstraints getDefault() {
		return new UiConstraints();
	}
	
	public static UiConstraints getRelative( float x, float y, float width, float height ) {
		return new UiConstraints( new RelativeConstraint( x ), new RelativeConstraint( y ), 
				new RelativeConstraint( width ), new RelativeConstraint( height ) );
	}
	
	public static UiConstraints getPixel( int x, int y, int width, int height ) {
		return new UiConstraints( new PixelConstraint( x ), new PixelConstraint( y ),
				new PixelConstraint( width ), new PixelConstraint( height ) );
	}
	
	public static UiConstraints getFill() {
		return getRelative( 0, 0, 1, 1 );
	}
	
}
