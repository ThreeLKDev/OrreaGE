package threelkdev.orreaGE.core.ui;

import threelkdev.orreaGE.core.rendering.openGL.textures.Texture;
import threelkdev.orreaGE.tools.colours.Colour;

public interface UiRenderData {
	
	public float getX();
	
	public float getY();
	
	public float getWidth();
	
	public float getHeight();
	
	public float getRoundedCornerRadius();
	
	public Texture getTexture();
	
	public Colour getOverrideColour();
	
	public Colour getBorderColour();
	
	public float getBorderWidth();
	
	public float getAlpha();
	
	public boolean isBlurry();
	
	public boolean isFlippedTexture();
	
	public int[] getClippingBounds();
	
	public void setClippingBounds( int[] bounds );
	
	public int getLevel();
}
