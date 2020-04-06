package threelkdev.orreaGE.core.ui;

import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;
import threelkdev.orreaGE.tools.colours.Colour;

public class UiBlock extends UiComponent implements UiRenderData {
	
	private static final int UNUSED = -1;
	
	private Texture texture = null;
	private Colour overrideColour = null;
	private Colour borderColour = null;
	
	private int[] clippingBounds = null;
	
	private boolean blurry = false;
	private boolean flipTexture = false;
	private float roundedCornerRadius = UNUSED;
	private float borderWidth = UNUSED;
	
	public UiBlock( Texture texture ) {
		this.texture = texture;
	}
	
	public UiBlock( Colour colour ) {
		this.overrideColour = colour;
		this.setAlpha( colour.getA() );
	}
	
	@Override
	public float getX() { return super.getAbsX(); }
	
	@Override
	public float getY() { return super.getAbsY(); }
	
	@Override
	public float getWidth() { return super.getAbsWidth(); }
	
	@Override
	public float getHeight() { return super.getAbsHeight(); }
	
	public void setRoundedCorners( float radius ) {
		this.roundedCornerRadius = radius;
	}
	
	public Texture getTexture() { return texture; }
	public Colour getOverrideColour() { return overrideColour; }
	public Colour getBorderColour() { return borderColour; }
	public float getAlpha() { return super.getTotalAlpha(); }
	public boolean isBlurry() { return blurry; }
	public boolean isFlippedTexture() { return flipTexture; }
	public int[] getClippingBounds() { return clippingBounds; }
	public float getRoundedCornerRadius() { return roundedCornerRadius; }
	public float getBorderWidth() { return borderWidth; }
	
	public void setColour( Colour colour ) {
		if( overrideColour == null )
			overrideColour = new Colour();
		this.overrideColour.setColour( colour );
		this.setAlpha( colour.getA() );
	}
	
	public void setBorderWidth( float width ) {
		this.borderWidth = width;
	}
	public void setBorder( Colour colour, float width ) { 
		if( borderColour == null )
			borderColour = new Colour();
		this.borderColour.set( colour );
		this.borderWidth = width;
	}
	
	@Override
	protected void getRenderData( UiRenderBundle renderData ) {
		renderData.addUiRenderData( this );
	}
	
	@Override 
	protected void onInit() {}
	
	@Override
	protected void updateSelf() {}

	@Override
	public void setClippingBounds(int[] bounds) {
		this.clippingBounds = bounds;
	}
	
}
