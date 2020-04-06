package threelkdev.orreaGE.core.ui.constraints;

import threelkdev.orreaGE.core.ui.UiComponent;
import threelkdev.orreaGE.tools.errors.ErrorManager;

public class UiConstraints {
	
	private UiConstraint xConstraint;
	private UiConstraint yConstraint;
	private UiConstraint widthConstraint;
	private UiConstraint heightConstraint;
	
	public UiConstraints() {}
	
	public void notifyAdded( UiComponent current, UiComponent parent ) {
		try {
			xConstraint.notifyAdded( this, current, parent );
			yConstraint.notifyAdded( this, current, parent );
			widthConstraint.notifyAdded( this, current, parent );
			heightConstraint.notifyAdded( this, current, parent );
		} catch ( Exception e ) {
			ErrorManager.crashWithUserAlert( "UI Setup Error", "Unable to set up the constraints for a UI.", e );
		}
	}
	
	public UiConstraints( UiConstraint x, UiConstraint y, UiConstraint width, UiConstraint height ) {
		setX( x );
		setY( y );
		setWidth( width );
		setHeight( height );
	}
	
	public UiConstraint getX() { return xConstraint; }
	public UiConstraint getY() { return yConstraint; }
	public UiConstraint getWidth() { return widthConstraint; }
	public UiConstraint getHeight() { return heightConstraint; }
	
	public void setX( UiConstraint constraint ) {
		this.xConstraint = constraint;
		constraint.setAxis( true, true );
	}
	
	public void setY( UiConstraint constraint ) {
		this.yConstraint = constraint;
		constraint.setAxis( false, true );
	}
	
	public void setWidth( UiConstraint constraint ) {
		this.widthConstraint = constraint;
		constraint.setAxis( true, false );
	}
	
	public void setHeight( UiConstraint constraint ) {
		this.heightConstraint = constraint;
		constraint.setAxis( false, false );
	}
	
	
}
